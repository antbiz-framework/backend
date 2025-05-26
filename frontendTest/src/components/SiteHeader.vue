<script setup lang="ts">
const authStore = useAuthStore()

const show: Ref<boolean> = ref(false)
</script>

<template>
  <nav class="bg-white shadow">
    <div class="container mx-auto px-4 py-4 flex justify-between items-center">
      <!-- 왼쪽 요소 -->
      <div class="text-lg font-bold">
        <NuxtLink to="/">
          <img src="/logo.png" alt="AntBizFramework">
        </NuxtLink>
      </div>

      <!-- 모바일 뷰 햄버거 버튼 -->
      <div class="block md:hidden">
        <button id="hamburger" class="focus:outline-none" type="button" @click="show = !show">
          <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16m-7 6h7"/></svg>
        </button>
      </div>

      <!-- 오른쪽 요소 -->
      <div class="hidden md:flex space-x-4 items-center" v-if="!authStore.isAuthenticated">
        <NuxtLink to="/intro" class="text-gray-700 hover:text-blue-500">소개</NuxtLink>
        <NuxtLink to="/service" class="text-gray-700 hover:text-blue-500">서비스</NuxtLink>
        <NuxtLink to="/login" class="text-gray-700 hover:text-blue-500">로그인</NuxtLink>
      </div>
      <div class="hidden md:flex space-x-4 items-center" v-else>
        <NuxtLink to="/intro" class="text-gray-700 hover:text-blue-500">소개</NuxtLink>
        <NuxtLink to="/service" class="text-gray-700 hover:text-blue-500">서비스</NuxtLink>
        <NuxtLink to="/logout" class="text-gray-700 hover:text-blue-500">로그아웃</NuxtLink>
        <div class="text-gray-700 flex flex-row gap-[20px] items-center">
          <span>{{ authStore?.userInfo?.username ?? "???" }}</span>
        </div>
      </div>
    </div>

    <!-- 모바일 메뉴 -->
    <div v-if="show" class="md:hidden bg-white overflow-hidden" style="max-height: 600px;">
      <!-- 오른쪽 요소 -->
      <div class="flex flex-col space-y-2 py-4 px-4" v-if="!authStore.isAuthenticated">
        <NuxtLink to="/intro" class="text-gray-700 hover:text-blue-500">소개</NuxtLink>
        <NuxtLink to="/service" class="text-gray-700 hover:text-blue-500">서비스</NuxtLink>
        <NuxtLink to="/login" class="text-gray-700 hover:text-blue-500">로그인</NuxtLink>
      </div>
      <div class="flex flex-col space-y-2 py-4 px-4" v-else>
        <NuxtLink to="/intro" class="text-gray-700 hover:text-blue-500">소개</NuxtLink>
        <NuxtLink to="/service" class="text-gray-700 hover:text-blue-500">서비스</NuxtLink>
        <NuxtLink to="/logout" class="text-gray-700 hover:text-blue-500">로그아웃</NuxtLink>
        <div class="text-gray-700 flex flex-row gap-[20px] items-center">
          <span>{{ authStore?.userInfo?.username ?? "???" }}</span>
        </div>
      </div>
    </div>
  </nav>
</template>
