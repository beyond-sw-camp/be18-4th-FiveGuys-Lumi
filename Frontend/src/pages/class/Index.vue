<template>
  <v-container fluid>
    <v-row>
      <v-col cols="8">
        <vue-cal
          style="height: 600px"
          view="month"
          :views="{ month: {} }"
          @cell-click="onDateClick"
        />
      </v-col>

      <v-col cols="4">
        <v-card class="pa-4 d-flex flex-column" style="max-height: 600px; min-height: 600px">
          <v-card-title class="text-subtitle-1 pa-0 ma-0">
            {{ editingId ? '일정 수정' : '일정 등록' }}
          </v-card-title>
          <v-form ref="formRef" v-model="isValid">
            <v-list density="compact">
              <v-list-item class="pa-0">
                <v-list-item-title class="text-body-2 pb-2">수업 시작</v-list-item-title>
                <v-text-field
                  v-model="form.startDate"
                  class="small-input pb-4"
                  density="compact"
                  label="수업 시작"
                  :rules="[validateStartDate]"
                  type="datetime-local"
                  variant="outlined"
                />
              </v-list-item>

              <v-list-item class="pa-0">
                <v-list-item-title class="text-body-2 pb-2">수업 종료</v-list-item-title>
                <v-text-field
                  v-model="form.endDate"
                  class="small-input pb-4"
                  density="compact"
                  label="수업 종료"
                  required
                  :rules="[validateEndDate]"
                  type="datetime-local"
                  variant="outlined"
                />
              </v-list-item>

              <v-list-item class="pa-0">
                <v-list-item-title class="text-body-2 pb-2">장소</v-list-item-title>
                <v-text-field
                  v-model="form.location"
                  class="small-input pb-4"
                  density="compact"
                  placeholder="장소를 입력해주세요."
                  variant="outlined"
                />
              </v-list-item>

              <v-list-item class="pa-0">
                <v-list-item-title class="text-body-2 pb-2">출결 상태</v-list-item-title>
                <v-select
                  v-model="form.statusType"
                  class="small-input pb-4"
                  density="compact"
                  :items="['SCHEDULED', 'ATTEND', 'ABSENT', 'ILLNESS']"
                  variant="outlined"
                />
              </v-list-item>
            </v-list>
            <v-spacer />

            <v-row class="mt-auto" justify="space-between" no-gutters>
              <v-btn class="flex-grow-1 mx-1" color="primary" variant="flat" @click="submitForm">
                {{ editingId ? '수정하기' : '등록하기' }}
              </v-btn>
              <v-btn class="flex-grow-1 mx-1" color="grey" variant="flat" @click="resetForm">
                취소
              </v-btn>
            </v-row>
          </v-form>
        </v-card>
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <v-card class="overflow-auto" style="max-height: 250px; min-height: 250px">
          <v-list class="py-0">
            <template v-if="schedule && schedule.length > 0">
              <template v-for="(item, i) in schedule" :key="`${item.entityType}-${item.entityId}`">
                <v-divider v-if="i !== 0" />

                <v-list-item>
                  <!-- 공통 헤더 -->
                  <v-list-item-title>
                    [{{ item.channelName }}] {{ item.entityType }} ({{ item.roleName }})
                  </v-list-item-title>

                  <v-list-item-subtitle>
                    일정 : {{ item.startDate || '-' }} ~ {{ item.endDate || '-' }}
                  </v-list-item-subtitle>
                  <v-list-item-subtitle> 장소 : {{ item.location || '-' }} </v-list-item-subtitle>
                  <v-list-item-subtitle>
                    출결 상태 : {{ item.statusType || '-' }}
                  </v-list-item-subtitle>

                  <!-- 수정/삭제 버튼 (내가 생성한 일정만) -->
                  <template #append>
                    <v-menu v-if="item.channelId === currentChannelId">
                      <template #activator="{ props }">
                        <v-btn icon="mdi-dots-vertical" variant="text" v-bind="props" @click.stop />
                      </template>
                      <v-list>
                        <v-list-item class="text-button" @click="editCourse(item)">
                          수정
                        </v-list-item>
                        <v-list-item class="text-button" @click="askDelete(item.entityId)">
                          삭제
                        </v-list-item>
                      </v-list>
                    </v-menu>
                  </template>
                </v-list-item>
              </template>
            </template>

            <!-- 일정 없음 -->
            <template v-else>
              <v-list-item>
                <v-list-item class="text-center text-grey">
                  <v-list-item-title>등록된 일정이 없습니다.</v-list-item-title>
                </v-list-item>
              </v-list-item>
            </template>
          </v-list>
        </v-card>
      </v-col>
    </v-row>

    <v-dialog v-model="showDeleteModal" max-width="400">
      <v-card>
        <v-card-title>일정 삭제</v-card-title>
        <v-card-text>일정을 정말 삭제하시겠습니까?</v-card-text>
        <v-card-actions>
          <v-spacer />
          <v-btn color="grey" text @click="cancelDelete">취소</v-btn>
          <v-btn color="red" text @click="confirmDelete">삭제하기</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { VueCal } from 'vue-cal';
import { useRoute } from 'vue-router';
import { getCalendar } from '@/apis/calendar';
import { createCourse, deleteCourse, updateCourse } from '@/apis/course';
import { useApi } from '@/composable/useApi';
import formatDate from '@/utils/formatDate';
import formatDateTime from '@/utils/formatDateTime';
import 'vue-cal/style';

const { data: calendar, queryFnExecute: useGetCalendar } = useApi(getCalendar);
const { queryFnExecute: useCreateCourse } = useApi(createCourse);
const { queryFnExecute: useUpdateCourse } = useApi(updateCourse);
const { queryFnExecute: useDeleteCourse } = useApi(deleteCourse);

const form = ref({
  startDate: '',
  endDate: '',
  location: '',
  statusType: 'SCHEDULED',
});

const formRef = ref(null);
const isValid = ref(false);

const currentChannelId = Number(useRoute().params.channelId);
const schedule = ref();
const editingId = ref(null);

const selectedDate = ref(formatDate(new Date()));

const showDeleteModal = ref(false);
const courseToDeleteId = ref(null);

onMounted(async () => {
  await refreshData();
});

async function refreshData(date = selectedDate.value) {
  await useGetCalendar(date);

  if (!Array.isArray(calendar.value)) return;

  schedule.value = calendar.value
    .filter(item => item.entityType === 'COURSE')
    .map(item => ({
      channelId: item.channelId,
      channelName: item.channelName,
      entityId: item.entityId,
      entityType: item.entityType,
      roleName: item.roleName,
      location: item.location,
      statusType: item.statusType,
      startDate: item.startDate,
      endDate: item.endDate,
    }));
}

function validateStartDate(value) {
  if (!value) return '시작 시간을 입력해주세요.';
  const start = new Date(value);
  if (start < new Date()) return '시작 시간은 현재 시간 이후여야 합니다.';
  return true;
}

function validateEndDate(value) {
  if (!value) return '종료 시간을 입력해주세요.';
  const end = new Date(value);
  const start = new Date(form.value.startDate);

  if (end <= new Date()) return '종료 시간은 현재 시간 이후여야 합니다.';
  if (start && end <= start) return '종료 시간은 시작 시간 이후여야 합니다.';
  return true;
}

async function submitForm() {
  const isFormValid = await formRef.value.validate();
  if (!isFormValid) return;
  try {
    const payload = {
      startDate: formatDateTime(new Date(form.value.startDate)),
      endDate: formatDateTime(new Date(form.value.endDate)),
      location: form.value.location,
      statusType: form.value.statusType,
    };

    await (editingId.value
      ? useUpdateCourse(currentChannelId, editingId.value, payload)
      : useCreateCourse(currentChannelId, payload));
    await refreshData();
    resetForm();
  } catch (error) {
    console.error('제출 실패', error);
  }
}

function resetForm() {
  editingId.value = null;
  form.value = {
    startDate: '',
    endDate: '',
    location: '',
    statusType: 'SCHEDULED',
  };
}

async function onDateClick({ cell }) {
  const clickedDate = formatDate(cell.start);
  selectedDate.value = clickedDate;
  await refreshData(clickedDate);
}

function editCourse(course) {
  editingId.value = course.entityId;

  form.value = {
    startDate: course.startDate,
    endDate: course.endDate,
    location: course.location,
    statusType: course.statusType,
  };
}

function askDelete(courseId) {
  showDeleteModal.value = true;
  courseToDeleteId.value = courseId;
}

async function confirmDelete() {
  const courseId = courseToDeleteId.value;
  if (!courseId) return;

  try {
    await useDeleteCourse(currentChannelId, courseId);
    await refreshData();
  } catch (error) {
    console.error('삭제 실패', error);
  } finally {
    showDeleteModal.value = false;
    courseToDeleteId.value = null;
  }
}

function cancelDelete() {
  showDeleteModal.value = false;
  courseToDeleteId.value = null;
}
</script>

<style lang="scss" scoped>
.vuecal {
  --vuecal-primary-color: #d4aaff;
  .vuecal__scrollable--month-view {
    .vuecal__event {
      height: 15px;
      margin-top: 1px;
    }
    .vuecal__event-details {
      font-size: 11px;
      white-space: nowrap;
      padding: 0;
    }

    .vuecal__cell--has-events {
      flex-direction: row-reverse;
      overflow: hidden;
      justify-content: flex-start;
    }

    .vuecal__cell--has-events .vuecal__cell-date {
      align-self: flex-start;
    }
  }
}

.small-input {
  padding: 0;
  :deep(.v-field__input) {
    min-height: 32px !important;
    font-size: 14px;
  }

  :deep(.v-overlay__content) {
    min-width: 120px !important;
    max-height: 150px !important;
  }

  :deep(.v-list-item-title) {
    font-size: 14px !important;
    padding: 2px 8px !important;
  }
}
</style>
