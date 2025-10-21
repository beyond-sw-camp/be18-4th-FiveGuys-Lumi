<template>
  <v-dialog
    :max-width="400"
    :model-value="modelValue"
    @update:model-value="emit('update:modelValue', $event)"
  >
    <v-card>
      <v-card-title>채널 삭제</v-card-title>
      <v-card-text>
        정말 <strong>{{ channel?.name }}</strong> 채널을 정말 삭제하시겠습니까?
      </v-card-text>
      <v-card-actions>
        <v-spacer />
        <v-btn text @click="$emit('update:modelValue', false)">취소하기</v-btn>
        <v-btn color="red" @click="confirmDelete">삭제</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
const emit = defineEmits(['delete', 'update:modelValue']);

const props = defineProps({
  modelValue: Boolean,
  channel: Object,
});

function confirmDelete() {
  emit('delete', props.channel);
  emit('update:modelValue', false);
}
</script>

<style lang="scss" scoped></style>
