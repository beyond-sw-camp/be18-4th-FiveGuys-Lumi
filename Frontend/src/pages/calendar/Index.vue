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
        <div>
          <div class="mb-2">할 일 ({{ selectedDate }})</div>
          <v-text-field v-model="newTask" label="할 일을 입력하세요." variant="solo">
            <template #append-inner>
              <v-fade-transition>
                <v-btn v-show="newTask" icon="mdi-plus-circle" variant="text" @click="createTask" />
              </v-fade-transition>
            </template>
          </v-text-field>

          <v-divider class="mb-4"></v-divider>

          <v-card class="overflow-auto" style="max-height: 470px; min-height: 470px">
            <v-list class="py-0">
              <template v-if="tasks.length > 0">
                <template v-for="(task, i) in tasks" :key="task.todoId">
                  <v-divider v-if="i !== 0" />

                  <v-list-item>
                    <template #prepend>
                      <v-checkbox-btn
                        color="grey"
                        :model-value="task.status"
                        @click.stop
                        @update:model-value="() => updateStatus(task)"
                      />
                    </template>

                    <!-- 제목 -->
                    <v-list-item-title>
                      <span v-if="editingTodoId === task.todoId">
                        <v-text-field
                          v-model="editingText"
                          dense
                          hide-details
                          variant="underlined"
                          @click.stop
                          @keydown.enter.prevent="saveEdit(task.todoId)"
                        />
                      </span>
                      <span v-else :class="{ 'text-grey': task.status }">
                        {{ task.description }}
                      </span>
                    </v-list-item-title>

                    <!-- 메뉴 -->
                    <template #append>
                      <v-menu>
                        <template #activator="{ props }">
                          <v-btn
                            icon="mdi-dots-vertical"
                            variant="text"
                            v-bind="props"
                            @click.stop
                          />
                        </template>
                        <v-list>
                          <v-list-item class="text-button" @click="editTask(task)">
                            수정
                          </v-list-item>
                          <v-list-item class="text-button" @click="askDelete(task.todoId)">
                            삭제
                          </v-list-item>
                        </v-list>
                      </v-menu>
                    </template>
                  </v-list-item>
                </template>
              </template>

              <template v-else>
                <v-list-item style="min-height: 500px">
                  <v-list-item-title class="text-center text-grey">
                    할 일이 없습니다.
                  </v-list-item-title>
                </v-list-item>
              </template>
            </v-list>
          </v-card>
        </div>
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <v-card class="overflow-auto" style="max-height: 250px; min-height: 250px">
          <v-list class="py-0">
            <!-- 일정 있음 -->
            <template v-if="schedule && schedule.length > 0">
              <template v-for="(item, i) in schedule" :key="`${item.entityType}-${item.entityId}`">
                <v-divider v-if="i !== 0" />

                <v-list-item>
                  <!-- 공통 헤더 -->
                  <v-list-item-title>
                    [{{ item.channelName }}] {{ item.entityType }} ({{ item.roleName }})
                  </v-list-item-title>

                  <!-- ASSIGNMENT -->
                  <v-list-item-subtitle
                    v-if="item.entityType === 'ASSIGNMENT'"
                    class="multi-line-subtitle"
                  >
                    마감일 : {{ item.deadlineAt || '-' }}
                    <br />
                    제출여부 : {{ item.isSubmission ? '제출완료' : '미제출' }}
                  </v-list-item-subtitle>

                  <!-- EVALUATION -->
                  <v-list-item-subtitle
                    v-else-if="item.entityType === 'EVALUATION'"
                    class="multi-line-subtitle"
                  >
                    마감일 : {{ item.evaluationDeadlineAt || '-' }}
                    <br />
                    제출여부 : {{ item.isSubmission ? '제출완료' : '미제출' }}
                  </v-list-item-subtitle>

                  <!-- COURSE -->
                  <v-list-item-subtitle v-else-if="item.entityType === 'COURSE'">
                    일정 : {{ item.startDate || '-' }} ~ {{ item.endDate || '-' }}
                  </v-list-item-subtitle>
                  <v-list-item-subtitle> 장소 : {{ item.location || '-' }} </v-list-item-subtitle>
                  <v-list-item-subtitle>
                    출결 상태 : {{ item.statusType || '-' }}
                  </v-list-item-subtitle>
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
        <v-card-title>할 일 삭제</v-card-title>
        <v-card-text>할 일을 정말 삭제하시겠습니까?</v-card-text>
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
import { createTodo, deleteTodo, getCalendar, getTodo, updateTodo } from '@/apis/calendar';
import { useApi } from '@/composable/useApi';
import formatDate from '@/utils/formatDate';
import 'vue-cal/style';

const tasks = ref([]);
const schedule = ref();
const newTask = ref(null);
const selectedDate = ref(formatDate(new Date()));

const editingTodoId = ref(null);
const editingText = ref('');
const showDeleteModal = ref(false);
const taskToDeleteId = ref(null);

const { data: todo, queryFnExecute: useGetTodo } = useApi(getTodo);
const { queryFnExecute: useCreateTodo } = useApi(createTodo);
const { queryFnExecute: useUpdateTodo } = useApi(updateTodo);
const { queryFnExecute: useDeleteTodo } = useApi(deleteTodo);
const { data: calendar, queryFnExecute: useGetCalendar } = useApi(getCalendar);

onMounted(async () => {
  await refreshData();
});

async function refreshData(date = selectedDate.value) {
  await useGetTodo(date);
  await useGetCalendar(date);

  if (!Array.isArray(todo.value)) return;
  if (!Array.isArray(calendar.value)) return;

  tasks.value = todo.value.map(item => ({
    todoId: item.todoId,
    description: item.description,
    status: item.status,
  }));

  schedule.value = calendar.value.map(item => ({
    channelId: item.channelId,
    channelName: item.channelName,
    entityId: item.entityId,
    entityType: item.entityType,
    roleName: item.roleName,
    location: item.location,
    statusType: item.statusType,
    startDate: item.startDate,
    endDate: item.endDate,
    deadlineAt: item.deadlineAt,
    isSubmission: item.isSubmission,
    evaluationDeadlineAt: item.evaluationDeadlineAt,
    isEvaluation: item.isEvaluation,
  }));
}

async function onDateClick({ cell }) {
  const clickedDate = formatDate(cell.start);
  selectedDate.value = clickedDate;
  await refreshData(clickedDate);
}

async function createTask() {
  if (!newTask.value) return;
  try {
    const created = await useCreateTodo({
      description: newTask.value,
      status: false,
      dueDate: selectedDate.value,
    });
    tasks.value.push(created[0]);
    newTask.value = null;
  } catch (error) {
    console.error('생성 실패', error);
  }
}

function editTask(task) {
  editingTodoId.value = task.todoId;
  editingText.value = task.description;
}

async function saveEdit(todoId) {
  const task = tasks.value.find(t => t.todoId === todoId);
  if (!task) return;

  try {
    await useUpdateTodo(todoId, { description: editingText.value });
    task.description = editingText.value;
  } catch (error) {
    console.error('수정 실패', error);
  } finally {
    editingTodoId.value = null;
  }
}

async function updateStatus(task) {
  try {
    const newStatus = !task.status;
    await useUpdateTodo(task.todoId, { status: newStatus });
    task.status = newStatus;
  } catch (error) {
    console.error('상태 변경 실패', error);
  }
}

function askDelete(todoId) {
  showDeleteModal.value = true;
  taskToDeleteId.value = todoId;
}

async function confirmDelete() {
  const todoId = taskToDeleteId.value;
  if (!todoId) return;

  try {
    await useDeleteTodo(todoId);
    tasks.value = tasks.value.filter(task => task.todoId !== todoId); // 즉시 반영
  } catch (error) {
    console.error('삭제 실패', error);
  } finally {
    showDeleteModal.value = false;
    taskToDeleteId.value = null;
  }
}

function cancelDelete() {
  showDeleteModal.value = false;
  taskToDeleteId.value = null;
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

.multi-line-subtitle {
  white-space: normal !important;
  overflow: visible !important;
  text-overflow: unset !important;
}
</style>
