import apiClient from '@/apis/apiClient';

// 자료 목록 조회
export async function getMaterials(channelId) {
  const { data } = await apiClient.get(`/channels/${channelId}/materials`);
  return data.data; // 배열 (MaterialsResponseDto[])
}

// 자료 단일 조회
export async function getMaterial(channelId, materialId) {
  const { data } = await apiClient.get(`/channels/${channelId}/materials/${materialId}`);
  console.log('getMaterial 응답:', data);
  return Array.isArray(data.data) ? data.data[0] : data.data; // 객체 (MaterialResponseDto)
}

// 자료 등록
export async function createMaterial(channelId, payload) {
  const { data } = await apiClient.post(`/channels/${channelId}/materials`, payload);
  return data.data; // 객체 (MaterialResponseDto)
}

// 자료 수정
export async function updateMaterial(channelId, materialId, payload) {
  const { data } = await apiClient.put(`/channels/${channelId}/materials/${materialId}`, payload);
  return data.data; // 객체 (MaterialResponseDto)
}

// 자료 삭제
export async function deleteMaterial(channelId, materialId) {
  const { data } = await apiClient.delete(`/channels/${channelId}/materials/${materialId}`);
  return data.data; // 객체 (MaterialResponseDto)
}
