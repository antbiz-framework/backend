<script setup lang="ts">
import {ref} from 'vue';
import ModalComponent from "~/components/ModalComponent.vue";

const email = ref('');
const password = ref('');
const showModal = ref(false);
const modalMessage = ref('');
const authStore = useAuthStore();

const handleLogin = async () => {
  try {
    await authStore.login(email.value, password.value);
    modalMessage.value = '로그인 성공!';
    showModal.value = true;
  } catch (error) {
    modalMessage.value = '로그인 실패: ' + error.message;
    showModal.value = true;
  }
};

const closeModal = (value: boolean) => {
  showModal.value = value;
};
</script>

<template>
  <div class="w-full h-full align-middle flex justify-center items-center">
    <div class="w-full h-full max-w-sm p-6 bg-white rounded-md shadow-md">
      <h2 class="text-2xl font-bold text-center text-gray-700">로그인</h2>
      <form class="mt-6" @submit.prevent="handleLogin">
        <div class="mb-4">
          <label for="email" class="block text-sm font-medium text-gray-600">
            이메일
          </label>
          <input v-model="email"
              type="email"
              id="email"
              class="w-full px-4 py-2 mt-2 text-gray-700 bg-gray-200 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400"
              placeholder="example@email.com"
          />
        </div>
        <div class="mb-6">
          <label for="password" class="block text-sm font-medium text-gray-600">
            비밀번호
          </label>
          <input v-model="password"
              type="password"
              id="password"
              class="w-full px-4 py-2 mt-2 text-gray-700 bg-gray-200 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400"
              placeholder="••••••••"
          />
        </div>
        <button
            type="submit"
            class="w-full px-4 py-2 text-white bg-blue-600 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-400"
        >
          로그인
        </button>
      </form>
      <p class="mt-4 text-sm text-center text-gray-600">
        계정이 없으신가요? <a href="#" class="text-blue-600 hover:underline">회원가입</a>
      </p>
    </div>
    <ModalComponent :isModalOpen="showModal" @update:isModalOpen="closeModal" />
  </div>
</template>

<style scoped>

</style>