<template>
  <v-dialog v-model="dialog" max-width="400">
    <v-card>
      <v-card-title class="text-h5"> 초대 코드로 채널 참가하기 </v-card-title>
      <v-card-text>
        <v-text-field
          v-model="invitationCode"
          label="초대 코드"
          placeholder="초대 코드를 입력하세요"
          clearable
        ></v-text-field>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="grey" text @click="closeModal">취소</v-btn>
        <v-btn color="#ffe8ff" text @click="joinChannel">참가</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref, watch } from 'vue';

const props = defineProps({
  modelValue: Boolean,
});

const emit = defineEmits(['update:modelValue', 'join']);

const dialog = ref(props.modelValue);
const invitationCode = ref('');

// 부모 컴포넌트의 modelValue가 변경되면 dialog 상태를 업데이트합니다.
watch(
  () => props.modelValue,
  newVal => {
    dialog.value = newVal;
  },
);

// dialog 상태가 변경되면 부모 컴포넌트로 이벤트를 보냅니다.
watch(dialog, newVal => {
  emit('update:modelValue', newVal);
});

// 참가 버튼 클릭 시
const joinChannel = () => {
  if (invitationCode.value.trim()) {
    emit('join', invitationCode.value);
    closeModal();
  }
};

// 모달 닫기
const closeModal = () => {
  dialog.value = false;
  invitationCode.value = ''; // 모달 닫을 때 입력값 초기화
};
</script>

<style scoped>
/* 필요한 경우 스타일 추가 */
</style>
