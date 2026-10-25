<template>
  <AdminLayout>
    <div class="space-y-6">
      <header class="flex flex-col gap-1 sm:flex-row sm:items-end sm:justify-between">
        <div>
          <p class="text-sm font-medium text-brand-600 first-letter:uppercase">{{ fechaTexto }}</p>
          <h1 class="font-body mt-1 text-3xl font-bold text-gray-900">Dashboard</h1>
        </div>
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
              <p class="font-body mt-3 text-3xl font-bold text-gray-900">{{ metric.value }}</p>
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
            <RouterLink to="/recepcion" class="text-sm font-medium text-brand-600"
              >Ver recepción</RouterLink
            >
          </div>
          <div class="mt-5 divide-y divide-gray-100">
            <div
              v-for="record in ultimosAccesos"
              :key="record.id"
              class="flex items-center gap-3 py-3 first:pt-0"
            >
              <span
                :class="
                  esIngreso(record) ? 'bg-brand-50 text-brand-600' : 'bg-error-50 text-error-600'
                "
                class="flex h-10 w-10 items-center justify-center rounded-full text-xs font-bold"
                >{{ esIngreso(record) ? 'OK' : 'X' }}</span
              >
              <div class="min-w-0 flex-1">
                <p class="text-sm font-semibold text-gray-800">
                  {{ record.nombreCliente }}
                </p>
                <p class="text-xs text-gray-500">
                  {{ record.fecha }} · {{ record.hora }} · {{ etiquetaAsistencia(record.estado) }}
                </p>
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
import { useGymData, esIngreso, etiquetaAsistencia, fechaHoy } from '@/composables/useGymData'
const { activeMembers, todaySessions, asistencia, ingresosDelMes } = useGymData()
const fechaTexto = new Date().toLocaleDateString('es-SV', {
  weekday: 'long',
  day: 'numeric',
  month: 'long',
  year: 'numeric',
})
const ultimosAccesos = computed(() => asistencia.value.slice(0, 8))
const asistenciasHoy = computed(
  () => asistencia.value.filter((record) => record.fecha === fechaHoy() && esIngreso(record)).length,
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
    value: `$${ingresosDelMes.value.toLocaleString('es-SV', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`,
    icon: '$',
    class: 'bg-blue-light-50 text-blue-light-600',
  },
  {
    label: 'Asistencias de hoy',
    value: asistenciasHoy.value,
    icon: '↗',
    class: 'bg-orange-50 text-orange-600',
  },
  {
    label: 'Sesiones de hoy',
    value: todaySessions.value,
    icon: '◷',
    class: 'bg-brand-50 text-brand-600',
  },
])
</script>
