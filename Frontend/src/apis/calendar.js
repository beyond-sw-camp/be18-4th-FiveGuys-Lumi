import apiClient from './apiClient';

export async function getTodos(startDate, endDate) {
  const { data } = await apiClient.get(`/todos?`, {
    params: { startDate, endDate },
  });

  return data.data;
}

export async function getTodo(date) {
  const { data } = await apiClient.get(`/todos/${date}`);
  return data.data;
}

export async function createTodo(param) {
  const { data } = await apiClient.post(`/todos`, {
    dueDate: param.dueDate,
    description: param.description,
  });
  return data.data;
}

export async function updateTodo(todoId, param) {
  const { data } = await apiClient.patch(`/todos/${todoId}`, {
    description: param.description,
    status: param.status,
  });
  return data.data;
}

export async function deleteTodo(todoId) {
  await apiClient.delete(`/todos/${todoId}`);
}

export async function getCalendars(startDate, endDate) {
  const { data } = await apiClient.get(`/calendars?`, {
    params: { startDate, endDate },
  });

  return data.data;
}

export async function getCalendar(date) {
  const { data } = await apiClient.get(`/calendars/${date}`);

  return data.data;
}
