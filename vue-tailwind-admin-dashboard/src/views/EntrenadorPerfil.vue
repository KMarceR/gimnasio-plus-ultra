<template>
  <AdminLayout
    ><div v-if="trainer" class="space-y-6">
      <button class="text-sm font-medium text-brand-600" @click="router.push('/entrenadores')">
        ← Volver a entrenadores
      </button>
      <section
        class="flex flex-col gap-4 rounded-2xl border border-gray-200 bg-white p-6 shadow-theme-xs sm:flex-row sm:items-center"
      >
        <span
          class="flex h-20 w-20 items-center justify-center rounded-2xl bg-brand-50 text-2xl font-semibold text-brand-600"
          >{{ trainer.initials }}</span
        >
        <div>
          <p class="text-sm text-gray-500">Perfil profesional</p>
          <h1 class="mt-1 text-3xl font-semibold text-gray-900">{{ trainer.name }}</h1>
          <p class="mt-1 text-brand-600">{{ trainer.specialty }} · ★ {{ trainer.rating }}</p>
        </div>
      </section>
      <div class="grid grid-cols-1 gap-6 lg:grid-cols-3">
        <section class="rounded-2xl border border-gray-200 bg-white p-6 lg:col-span-2">
          <h2 class="text-lg font-semibold text-gray-900">Información del entrenador</h2>
          <dl class="mt-5 grid gap-5 sm:grid-cols-2">
            <div>
              <dt class="text-xs text-gray-500">Email</dt>
              <dd class="mt-1 text-sm font-medium text-gray-800">{{ trainer.email }}</dd>
            </div>
            <div>
              <dt class="text-xs text-gray-500">Teléfono</dt>
              <dd class="mt-1 text-sm font-medium text-gray-800">{{ trainer.phone }}</dd>
            </div>
            <div>
              <dt class="text-xs text-gray-500">Horario</dt>
              <dd class="mt-1 text-sm font-medium text-gray-800">{{ trainer.schedule }}</dd>
            </div>
            <div>
              <dt class="text-xs text-gray-500">Tarifa por sesión</dt>
              <dd class="mt-1 text-sm font-medium text-gray-800">${{ trainer.rate }}</dd>
            </div>
          </dl>
          <h3 class="mt-8 font-semibold text-gray-900">Biografía</h3>
          <p class="mt-2 text-sm leading-6 text-gray-600">{{ trainer.bio }}</p>
        </section>
        <section class="rounded-2xl border border-gray-200 bg-white p-6">
          <h2 class="text-lg font-semibold text-gray-900">Métricas del mes</h2>
          <div class="mt-5 space-y-4">
            <div class="rounded-xl bg-gray-50 p-4">
              <p class="text-xs text-gray-500">Sesiones realizadas</p>
              <p class="mt-1 text-2xl font-semibold text-gray-900">42</p>
            </div>
            <div class="rounded-xl bg-gray-50 p-4">
              <p class="text-xs text-gray-500">Clientes activos</p>
              <p class="mt-1 text-2xl font-semibold text-gray-900">{{ trainer.clients }}</p>
            </div>
            <div class="rounded-xl bg-gray-50 p-4">
              <p class="text-xs text-gray-500">Ingresos generados</p>
              <p class="mt-1 text-2xl font-semibold text-gray-900">$18,900</p>
            </div>
          </div>
        </section>
      </div>
    </div>
    <div v-else class="p-8 text-center text-gray-500">Entrenador no encontrado.</div></AdminLayout
  >
</template>
<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router'
import AdminLayout from '@/components/layout/AdminLayout.vue'
import { useGymData } from '@/composables/useGymData'
const route = useRoute()
const router = useRouter()
const { trainerById } = useGymData()
const trainer = trainerById(String(route.params.id))
</script>
