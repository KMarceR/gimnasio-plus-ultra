<template>
  <div class="min-h-screen bg-gray-50">
    <AppSidebar />
    <main class="ms-60 min-h-screen p-4 md:p-6">
      <Alert
        v-if="errorCarga"
        class="mb-6"
        variant="error"
        title="No se pudieron cargar los datos"
        :message="errorCarga"
      />
      <slot></slot>
    </main>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import AppSidebar from './AppSidebar.vue'
import Alert from '@/components/ui/Alert.vue'
import { useGymData } from '@/composables/useGymData'

const { errorCarga, cargarDatos } = useGymData()
onMounted(cargarDatos)
</script>
