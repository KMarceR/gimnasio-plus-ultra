<template>
  <div class="min-h-screen bg-gray-50">
    <AppSidebar />
    <main class="ms-60 min-h-screen p-4 md:p-6">
      <slot></slot>
    </main>
  </div>
</template>

<script setup lang="ts">
import AppSidebar from './AppSidebar.vue'
</script>
