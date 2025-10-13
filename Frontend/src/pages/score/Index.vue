<template>
  <div>
    <!-- 카테고리 선택 -->
    <v-select
      v-model="selectedCategory"
      class="small-input"
      density="compact"
      :items="['전체', '모의고사', '내신', '기타']"
      style="max-width: 120px"
      variant="outlined"
      @update:model-value="updateChart"
    />

    <!-- 차트 -->
    <canvas ref="chartCanvas"></canvas>

    <!-- 등록 버튼 -->
    <div v-if="channel?.roleName === 'TUTOR'" class="d-flex justify-end mt-4">
      <v-btn color="primary" elevation="0" @click="openCreateModal">성적 등록하기</v-btn>
    </div>

    <!-- 데이터 테이블 -->
    <template v-if="flatData.length > 0">
      <div class="table-wrapper" style="min-height: 200px; max-height: 100%">
        <v-table class="text-center">
          <thead>
            <tr>
              <th>날짜</th>
              <th>제목</th>
              <th>점수</th>
              <th>카테고리</th>
              <th style="width: 50px"></th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="row in flatData" :key="row.gradeId">
              <td>{{ row.date }}</td>
              <td>{{ row.title }}</td>
              <td>{{ row.grade }}</td>
              <td>
                <v-chip
                  :color="getCategoryColor(row.category)"
                  size="small"
                  text-color="white"
                  variant="outlined"
                >
                  {{ row.category }}
                </v-chip>
              </td>
              <td v-if="channel?.roleName === 'TUTOR'" class="text-right">
                <v-menu>
                  <template #activator="{ props }">
                    <v-btn v-bind="props" icon size="small" variant="text">
                      <v-icon>mdi-dots-vertical</v-icon>
                    </v-btn>
                  </template>
                  <v-list>
                    <v-list-item class="text-button" @click="openEdit(row)">수정</v-list-item>
                    <v-list-item class="text-button" @click="openDelete(row)">삭제</v-list-item>
                  </v-list>
                </v-menu>
              </td>
            </tr>
          </tbody>
        </v-table>
      </div>
    </template>

    <template v-else>
      <v-list-item style="min-height: 200px; max-height: 100%">
        <v-list-item class="text-center text-grey">
          <v-list-item-title>등록된 성적이 없습니다.</v-list-item-title>
        </v-list-item>
      </v-list-item>
    </template>

    <v-dialog v-model="formDialog" max-width="400px">
      <v-card>
        <v-card-title>{{ editingId ? '성적 수정' : '성적 등록' }}</v-card-title>
        <v-card-text>
          <v-form ref="formRef">
            <v-text-field
              v-model="form.title"
              class="small-input pb-4"
              density="compact"
              label="제목"
              :rules="[v => !!v || '제목을 입력하세요.']"
              variant="outlined"
            />
            <v-text-field
              v-model.number="form.grades"
              class="small-input pb-4"
              density="compact"
              label="점수"
              :rules="[
                v => !!v || '점수를 입력하세요.',
                v => (v >= 0 && v <= 100) || '0~100 사이여야 합니다.',
              ]"
              type="number"
              variant="outlined"
            />
            <v-text-field
              v-model="form.date"
              class="small-input pb-4"
              density="compact"
              label="날짜"
              :rules="[v => !!v || '날짜를 선택하세요.']"
              type="date"
              variant="outlined"
            />
            <v-select
              v-model="form.category"
              class="small-input pb-4"
              density="compact"
              :items="['모의고사', '내신', '기타']"
              label="카테고리"
              :rules="[v => !!v || '카테고리를 선택하세요.']"
              variant="outlined"
            />
          </v-form>
        </v-card-text>
        <v-card-actions>
          <v-spacer />
          <v-btn text @click="formDialog = false">취소</v-btn>
          <v-btn color="primary" @click="submitForm">
            {{ editingId ? '수정하기' : '등록하기' }}
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- 삭제 모달 -->
    <v-dialog v-model="deleteDialog" max-width="300px">
      <v-card>
        <v-card-title>성적 삭제</v-card-title>
        <v-card-text>성적을 정말 삭제하시겠습니까?</v-card-text>
        <v-card-actions>
          <v-spacer />
          <v-btn text @click="deleteDialog = false">취소</v-btn>
          <v-btn color="error" @click="confirmDelete">삭제하기</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script setup>
import Chart from 'chart.js/auto';
import { computed, onBeforeUnmount, onMounted, ref, watch } from 'vue';
import { useRoute } from 'vue-router';
import { getChannel } from '@/apis/channel';
import { createGrade, deleteGrade, getGrades, updateGrade } from '@/apis/score';
import { useApi } from '@/composable/useApi';
import { CATEGORY_COLORS } from '@/constants/categoryColors';

const { data: grades, queryFnExecute: useGetGrades } = useApi(getGrades);
const { data: channel, queryFnExecute: useGetChannel } = useApi(getChannel);
const { queryFnExecute: useCreateGrade } = useApi(createGrade);
const { queryFnExecute: useUpdateGrade } = useApi(updateGrade);
const { queryFnExecute: useDeleteGrade } = useApi(deleteGrade);

const channelId = Number(useRoute().params.channelId);

const selectedCategory = ref('전체');
const chartCanvas = ref(null);
let chartInstance = null;
const formRef = ref(null);

const editingId = ref(null);
const formDialog = ref(false);
const form = ref({
  title: '',
  grades: null,
  date: '',
  category: '',
});

const deleteDialog = ref(false);
const deleteTarget = ref(null);

const flatData = computed(() => {
  if (!grades.value) return [];
  if (selectedCategory.value === '전체') {
    return grades.value.flatMap(d => d.grades);
  }
  return grades.value.filter(d => d.category === selectedCategory.value).flatMap(d => d.grades);
});

// 차트 데이터
function getChartData(category) {
  if (!grades.value) return { labels: [], datasets: [] };

  if (category === '전체') {
    const allGrades = grades.value.flatMap(d => d.grades);
    const labels = Array.from(new Set(allGrades.map(g => g.date))).sort();

    const datasets = grades.value.map(d => ({
      label: d.category,
      data: labels.map(labelDate => {
        const found = d.grades.find(g => g.date.startsWith(labelDate));
        return found ? found.grade : null;
      }),
      borderColor: CATEGORY_COLORS[d.category] || CATEGORY_COLORS.default,
      tension: 0.2,
      fill: false,
    }));
    return { labels, datasets };
  }

  const categoryData = grades.value.find(d => d.category === category);
  if (!categoryData) return { labels: [], datasets: [] };

  const labels = categoryData.grades.map(g => g.date);
  const dataset = {
    label: category,
    data: categoryData.grades.map(g => g.grade),
    borderColor: CATEGORY_COLORS[category] || CATEGORY_COLORS.default,
    fill: false,
    tension: 0.2,
  };

  return { labels, datasets: [dataset] };
}

function getCategoryColor(category) {
  return CATEGORY_COLORS[category] || CATEGORY_COLORS.default;
}

function renderChart(category) {
  const { labels, datasets } = getChartData(category);
  if (chartInstance) chartInstance.destroy();

  chartInstance = new Chart(chartCanvas.value, {
    type: 'line',
    data: { labels, datasets },
    options: {
      responsive: true,
      spanGaps: true,
      scales: { y: { min: 0, max: 100, ticks: { stepSize: 10 } } },
    },
  });
}

function updateChart() {
  renderChart(selectedCategory.value);
}

function openCreateModal() {
  editingId.value = null;
  form.value = { title: '', grades: null, date: '', category: '' };
  formDialog.value = true;
}

function openEdit(row) {
  editingId.value = row.gradeId;
  form.value = {
    grades: row.grade,
    date: row.date,
    title: row.title,
    category: row.category,
  };
  formDialog.value = true;
}

async function submitForm() {
  const { valid } = await formRef.value.validate();

  if (!valid) return;

  try {
    await (editingId.value
      ? useUpdateGrade(channelId, editingId.value, form.value)
      : useCreateGrade(channelId, form.value));
    await useGetGrades(channelId);
  } catch (error) {
    console.error(editingId.value ? '수정 실패:' : '생성 실패:', error);
  } finally {
    formDialog.value = false;
  }
}

function openDelete(row) {
  deleteTarget.value = row;
  deleteDialog.value = true;
}

async function confirmDelete() {
  try {
    await useDeleteGrade(channelId, deleteTarget.value.gradeId);
    await useGetGrades(channelId);
  } catch (error) {
    console.error('삭제 실패:', error);
  } finally {
    deleteDialog.value = false;
  }
}

onMounted(async () => {
  await useGetGrades(channelId);
  await useGetChannel(channelId);
  renderChart(selectedCategory.value);
});

watch(grades, newVal => {
  if (newVal && newVal.length > 0) {
    renderChart(selectedCategory.value);
  }
});

onBeforeUnmount(() => {
  if (chartInstance) chartInstance.destroy();
});
</script>

<style lang="scss" scoped>
.small-input {
  padding: 0;
  :deep(.v-field__input) {
    min-height: 32px !important;
    font-size: 14px;
  }
}

.table-wrapper {
  th {
    position: sticky;
    top: 0;
    background: white;
    z-index: 2;
  }
  tbody {
    display: block;
    max-height: 168px;
    overflow-y: auto;
  }
  tr {
    display: table;
    width: 100%;
    table-layout: fixed;
  }
}
</style>
