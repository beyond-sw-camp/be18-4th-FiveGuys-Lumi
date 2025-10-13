import apiClient from './apiClient';

// 평가 생성
export async function createEvaluation(channelId, submissionId, payload) {
  const { data } = await apiClient.post(
    `/channels/${channelId}/assignments/submissions/${submissionId}/evaluation`,
    payload,
  );
  return Array.isArray(data.data) ? data.data[0] : data.data;
}

// 평가 단일 조회
export async function getEvaluation(channelId, submissionId) {
  const { data } = await apiClient.get(
    `/channels/${channelId}/assignments/submissions/${submissionId}/evaluation`,
  );
  return Array.isArray(data.data) ? data.data[0] : data.data;
}

// 평가 수정
export async function updateEvaluation(channelId, submissionId, payload) {
  const { data } = await apiClient.put(
    `/channels/${channelId}/assignments/submissions/${submissionId}/evaluation`,
    payload,
  );
  return Array.isArray(data.data) ? data.data[0] : data.data;
}

// 평가 삭제
export async function deleteEvaluation(channelId, submissionId) {
  const { data } = await apiClient.delete(
    `/channels/${channelId}/assignments/submissions/${submissionId}/evaluation`,
  );
  return data.data;
}
