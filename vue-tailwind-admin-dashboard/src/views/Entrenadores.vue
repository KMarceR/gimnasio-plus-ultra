<template>
  <AdminLayout
    ><div class="space-y-6">
      <header class="flex items-end justify-between">
        <div>
          <p class="text-sm font-medium text-brand-600">Equipo operativo</p>
          <h1 class="mt-1 text-3xl font-semibold text-gray-900">Entrenadores</h1>
        </div>
        <button class="rounded-lg bg-brand-500 px-4 py-2.5 text-sm font-medium text-white">
          + Nuevo Entrenador
        </button>
      </header>
      <div class="grid grid-cols-1 gap-5 md:grid-cols-2 xl:grid-cols-3">
        <article
          v-for="trainer in trainers"
          :key="trainer.id"
          class="rounded-2xl border border-gray-200 bg-white p-6 shadow-theme-xs"
        >
          <div class="flex items-start justify-between">
            <span
              class="flex h-14 w-14 items-center justify-center rounded-2xl bg-brand-50 text-lg font-semibold text-brand-600"
              >{{ trainer.initials }}</span
            ><span
              :class="
                trainer.active ? 'bg-success-50 text-success-600' : 'bg-gray-100 text-gray-500'
              "
              class="rounded-full px-2.5 py-1 text-xs font-medium"
              >{{ trainer.active ? 'Activo' : 'Inactivo' }}</span
            >
          </div>
          <h2 class="mt-5 text-lg font-semibold text-gray-900">{{ trainer.name }}</h2>
          <p class="mt-1 text-sm text-brand-600">{{ trainer.specialty }}</p>
          <div class="mt-5 grid grid-cols-2 gap-3 text-sm">
            <div>
              <p class="text-gray-500">Rating</p>
              <p class="mt-1 font-semibold text-gray-800">★ {{ trainer.rating }}</p>
            </div>
            <div>
              <p class="text-gray-500">Clientes</p>
              <p class="mt-1 font-semibold text-gray-800">{{ trainer.clients }}</p>
            </div>
            <div>
              <p class="text-gray-500">Tarifa</p>
              <p class="mt-1 font-semibold text-gray-800">${{ trainer.rate }}</p>
            </div>
            <div>
              <p class="text-gray-500">Horario</p>
              <p class="mt-1 truncate font-semibold text-gray-800">{{ trainer.schedule }}</p>
            </div>
          </div>
          <RouterLink
            :to="`/entrenadores/${trainer.id}`"
            class="mt-6 block w-full rounded-lg border border-gray-200 py-2.5 text-center text-sm font-medium text-gray-700 hover:bg-gray-50"
            >Ver perfil</RouterLink
          >
        </article>
      </div>
    </div></AdminLayout
  >
</template>
<script setup lang="ts">
import AdminLayout from '@/components/layout/AdminLayout.vue'
import { useGymData } from '@/composables/useGymData'
const { trainers } = useGymData()
</script>
