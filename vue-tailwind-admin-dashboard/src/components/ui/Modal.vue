<template>
  <div class="fixed inset-0 flex items-center justify-center overflow-y-auto z-999999">
    <div
      v-if="fullScreenBackdrop"
      class="fixed inset-0 h-full w-full bg-gray-400/50 backdrop-blur-[32px] z-99999"
      aria-hidden="true"
      @click="$emit('close')"
    ></div>
    <div class="relative z-999999">
      <slot name="body"></slot>
    </div>
  </div>
</template>

<script setup lang="ts">
interface ModalProps {
  fullScreenBackdrop?: boolean
}

defineProps<ModalProps>()
defineEmits(['close'])
</script>
