<template>
  <AdminLayout>
    <div class="space-y-6">
      <header class="flex flex-col gap-1 sm:flex-row sm:items-end sm:justify-between">
        <div>
          <p class="text-sm font-medium text-brand-600">Lunes, 14 de julio de 2026</p>
          <h1 class="font-display mt-1 text-3xl font-bold text-gray-900">Dashboard</h1>
        </div>
        <span class="rounded-lg bg-brand-50 px-3 py-2 text-sm font-medium text-brand-700"
          >Operación en tiempo real</span
        >
      </header>
      <section class="grid grid-cols-1 gap-4 sm:grid-cols-2 xl:grid-cols-4">
        <article
          v-for="metric in metrics"
          :key="metric.label"
          class="rounded-2xl border border-gray-200 bg-white p-5 shadow-theme-xs"
        >
          <div class="flex items-start justify-between">
            <div>
              <p class="text-xs font-semibold uppercase tracking-wider text-gray-500">
                {{ metric.label }}
              </p>
              <p class="font-display mt-3 text-3xl font-bold text-gray-900">{{ metric.value }}</p>
            </div>
            <span
              :class="[
                'flex h-11 w-11 items-center justify-center rounded-xl text-lg',
                metric.class,
              ]"
              >{{ metric.icon }}</span
            >
          </div>
        </article>
      </section>
      <section class="grid grid-cols-1 gap-6 xl:grid-cols-1">
        <div class="rounded-2xl border border-gray-200 bg-white p-5 shadow-theme-xs sm:p-6">
          <div class="flex items-center justify-between">
            <h2 class="text-lg font-semibold text-gray-900">Últimos accesos</h2>
            <RouterLink to="/qr" class="text-sm font-medium text-brand-600"
              >Ver control QR</RouterLink
            >
          </div>
          <div class="mt-5 divide-y divide-gray-100">
            <div
              v-for="record in asistencia"
              :key="record.id"
              class="flex items-center gap-3 py-3 first:pt-0"
            >
              <span
                :class="
                  record.estado === 'Activo' || record.estado === 'Completada'
                    ? 'bg-brand-50 text-brand-600'
                    : 'bg-error-50 text-error-600'
                "
                class="flex h-10 w-10 items-center justify-center rounded-full text-xs font-bold"
                >{{ record.estado === 'Activo' || record.estado === 'Completada' ? 'OK' : 'X' }}</span
              >
              <div class="min-w-0 flex-1">
                <p class="text-sm font-semibold text-gray-800">
                  {{ memberById(record.clienteId) ? nombreCompleto(memberById(record.clienteId)!) : 'Cliente eliminado' }}
                </p>
                <p class="text-xs text-gray-500">{{ record.fecha }} · {{ record.hora }}</p>
              </div>
            </div>
            <p v-if="!asistencia.length" class="py-6 text-center text-sm text-gray-500">
              Sin registros de asistencia.
            </p>
          </div>
        </div>
      </section>
    </div>
  </AdminLayout>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import AdminLayout from '@/components/layout/AdminLayout.vue'
import { useGymData, nombreCompleto } from '@/composables/useGymData'
const { members, activeMembers, todaySessions, asistencia, memberById, planById } = useGymData()
const ingresosDelMes = computed(() =>
  members.value
    .filter((member) => member.estado === 'Activo')
    .reduce((total, member) => total + (planById(member.planId)?.precio || 0), 0),
)
const metrics = computed(() => [
  {
    label: 'Clientes activos',
    value: activeMembers.value,
    icon: '◎',
    class: 'bg-brand-50 text-brand-600',
  },
  {
    label: 'Ingresos del mes',
    value: `$${ingresosDelMes.value.toLocaleString('es-SV')}`,
    icon: '$',
    class: 'bg-blue-light-50 text-blue-light-600',
  },
  {
    label: 'Asistencias registradas',
    value: asistencia.value.length,
    icon: '↗',
    class: 'bg-orange-50 text-orange-600',
  },
  {
    label: 'Sesiones programadas',
    value: todaySessions.value,
    icon: '◷',
    class: 'bg-brand-50 text-brand-600',
  },
])

</script>
