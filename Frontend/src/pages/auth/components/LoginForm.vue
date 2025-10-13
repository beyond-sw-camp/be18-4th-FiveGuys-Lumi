<template>
  <div class="control">
    <v-form class="control" @submit.prevent="submitClick">
      <v-text-field
        v-model="formData.email"
        label="Email"
        required
        style="width: 400px; height: 60px"
        type="text"
        variant="outlined"
      />
      <v-text-field
        v-model="formData.password"
        :append-inner-icon="showPassword ? 'mdi-eye-off' : 'mdi-eye'"
        label="Password"
        required
        style="width: 400px; height: 60px"
        :type="showPassword ? 'text' : 'password'"
        variant="outlined"
        @click:append-inner="togglePassword"
      />
      <v-btn block color="primary" type="submit">로그인</v-btn>
    </v-form>
  </div>
</template>

<script setup>
import { reactive, ref, toRaw } from 'vue';

const formData = reactive({
  email: '',
  password: '',
});

const emit = defineEmits(['form-submit']);

const showPassword = ref(false);
function togglePassword() {
  showPassword.value = !showPassword.value;
}

function submitClick() {
  emit('form-submit', toRaw(formData));
}
</script>

<style scoped>
.control {
  display: flex;
  align-items: center;
  flex-direction: column;
  gap: 20px;
}
</style>
