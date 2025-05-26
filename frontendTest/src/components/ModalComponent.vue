<template>
  <div
      v-if="isModalOpen"
      class="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50"
  >
    <div class="bg-white rounded-lg shadow-lg p-6 w-full max-w-md">
      <header class="flex justify-between items-center mb-4">
        <h2 class="text-lg font-semibold">{{ title }}</h2>
        <button
            class="text-gray-500 hover:text-gray-700"
            @click="closeModal"
        >
          &times;
        </button>
      </header>
      <div class="mb-4">
        <slot></slot>
      </div>
      <footer class="flex justify-end">
        <button
            class="px-4 py-2 bg-gray-700 text-white rounded hover:bg-gray-800"
            @click="closeModal"
        >
          Close
        </button>
      </footer>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    isModalOpen: {
      type: Boolean,
      required: true
    },
    title: {
      type: String,
      default: "Modal Title"
    }
  },
  emits: ["update:isModalOpen"],
  methods: {
    closeModal() {
      this.$emit("update:isModalOpen", false);
    }
  }
};
</script>
