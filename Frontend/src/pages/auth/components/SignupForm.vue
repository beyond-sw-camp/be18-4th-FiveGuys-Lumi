<template>
  <v-form style="max-width: 450px; margin: auto" @submit.prevent="submitClick">
    <v-text-field
      v-model="formData.name"
      label="이름을 입력해주세요."
      required
      style="width: 100%; height: 50px; margin-bottom: 12px"
      type="text"
      variant="outlined"
    />

    <v-row align="center" class="mt-2" no-gutters>
      <v-col cols="8">
        <v-text-field
          v-model="formData.email"
          label="이메일을 입력해주세요."
          required
          style="height: 50px"
          type="text"
          variant="outlined"
        />
        <div
          v-if="formData.email"
          :style="{
            color: emailValid ? 'green' : 'red',
            marginTop: '6px',
            marginBottom: '12px',
            fontWeight: 'bold',
          }"
        >
          올바른 이메일 형식을 입력하세요.
        </div>
      </v-col>
      <v-col class="d-flex" cols="4">
        <v-btn
          block
          color="#eeddff"
          style="height: 50px; margin-left: 8px"
          @click="handleSendEmail"
        >
          <span v-if="!isSending">인증 전송</span>
          <span v-else>{{ countdown }}초 후 재전송</span>
        </v-btn>
      </v-col>
    </v-row>

    <v-text-field
      v-model="formData.password"
      label="비밀번호를 입력해주세요."
      required
      style="width: 100%; height: 50px; margin-top: 12px; margin-bottom: 6px"
      type="password"
      variant="outlined"
    />

    <div
      v-if="formData.password"
      :style="{ color: passwordValid ? 'green' : 'red', marginBottom: '12px', fontWeight: 'bold' }"
    >
      비밀번호는 8자 이상, 영문/숫자/특수문자를 모두 포함해야 합니다.
    </div>

    <v-text-field
      v-model="formData.passwordConfirm"
      label="비밀번호 확인"
      required
      style="width: 100%; height: 50px; margin-bottom: 12px"
      type="password"
      variant="outlined"
    />

    <div
      v-if="formData.passwordConfirm"
      :style="{ color: passwordMatch ? 'green' : 'red', fontWeight: 'bold', marginBottom: '12px' }"
    >
      {{ passwordMatch ? '비밀번호 일치합니다.' : '비밀번호 불일치합니다.' }}
    </div>

    <v-checkbox
      v-model="formData.agree"
      label="개인정보 수집 및 이용에 동의합니다."
      required
      style="margin-bottom: 16px"
    />

    <v-row align="center" class="mt-2 justify-center">
      <v-btn
        color="#eeddff"
        style="height: 50px; width: 48%; font-weight: bold"
        @click="handleSignUp"
      >
        회원가입 완료
      </v-btn>
    </v-row>
  </v-form>
</template>

<script setup>
import { computed, reactive, ref, toRaw } from 'vue';
import { useAuthStore } from '@/stores/authStore';

const authStore = useAuthStore();

const isSending = ref(false);
const countdown = ref(0);

const formData = reactive({
  name: '',
  email: '',
  password: '',
  passwordConfirm: '',
  agree: false,
});

const emailValid = computed(() =>
  /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,6}$/.test(formData.email),
);
const passwordValid = computed(() =>
  /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*]).{8,}$/.test(formData.password),
);
const passwordMatch = computed(() => formData.password === formData.passwordConfirm);

const emit = defineEmits(['switch-to-login']);

function goBack() {
  emit('switch-to-login');
}

async function handleSendEmail() {
  if (isSending.value) return;

  try {
    isSending.value = true;
    countdown.value = 10;

    alert('이메일 인증 코드가 전송되었습니다.');
    await authStore.sendEmail(formData.email);
    const timer = setInterval(() => {
      countdown.value -= 1;
      if (countdown.value <= 0) {
        clearInterval(timer);
        isSending.value = false;
      }
    }, 1000);
  } catch (error) {
    alert(error.message || '이메일 전송에 실패했습니다.');
  }
}

async function handleSignUp() {
  try {
    await authStore.signUp(toRaw(formData));
    alert('회원가입 성공했습니다.');
    emit('switch-to-login');
  } catch (error) {
    // 백엔드에서 던진 예외 메시지 그대로 사용
    alert(error.response?.data?.message || '회원가입 실패했습니다.');
  }
}

function submitClick() {}
</script>
