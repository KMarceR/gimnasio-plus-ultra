<template>
  <div :class="wide ? 'sm:col-span-2' : ''">
    <p class="text-xs font-semibold uppercase tracking-wider text-gray-500">{{ label }}</p>
    <div class="mt-2 rounded-xl border border-gray-200 bg-gray-50 px-4 py-3 text-sm text-gray-800">{{ value }}</div>
  </div>
</template>

<script setup lang="ts">
defineProps<{ label: string; value: string; wide?: boolean }>()
</script>
