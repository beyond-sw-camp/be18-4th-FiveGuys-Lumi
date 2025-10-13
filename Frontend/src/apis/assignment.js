import apiClient from '@/apis/apiClient';

// 목록 조회
export async function getAssignments(channelId) {
  const { data } = await apiClient.get(`/channels/${channelId}/assignments`);
  return data.data; // 배열 (AssignmentsResponseDto[])
}

// 단일 조회
export async function getAssignment(channelId, assignmentId) {
  const { data } = await apiClient.get(`/channels/${channelId}/assignments/${assignmentId}`);
  console.log('getAssignment 응답:', data);
  return Array.isArray(data.data) ? data.data[0] : data.data; // 객체 (AssignmentResponseDto)
}

// 등록
export async function createAssignment(channelId, payload) {
  const { data } = await apiClient.post(`/channels/${channelId}/assignments`, payload);
  return data.data; // 객체 (AssignmentResponseDto)
}

// 수정
export async function updateAssignment(channelId, assignmentId, payload) {
  const { data } = await apiClient.put(
    `/channels/${channelId}/assignments/${assignmentId}`,
    payload,
  );
  return data.data; // 객체 (AssignmentResponseDto)
}

// 삭제
export async function deleteAssignment(channelId, assignmentId) {
  const { data } = await apiClient.delete(`/channels/${channelId}/assignments/${assignmentId}`);
  return data.data; // 객체 (AssignmentResponseDto)
}
