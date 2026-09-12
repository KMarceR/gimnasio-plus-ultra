<template>
  <AdminLayout>
    <div class="space-y-6">
      <header class="flex flex-col gap-1 sm:flex-row sm:items-end sm:justify-between">
        <div>
          <p class="text-sm font-medium text-brand-600">Lunes, 14 de julio de 2026</p>
          <h1 class="mt-1 text-3xl font-semibold text-gray-900">Dashboard</h1>
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
              <p class="mt-3 text-3xl font-semibold text-gray-900">{{ metric.value }}</p>
            </div>
            <span
              :class="[
                'flex h-11 w-11 items-center justify-center rounded-xl text-lg',
                metric.class,
              ]"
              >{{ metric.icon }}</span
            >
          </div>
          <p class="mt-4 text-sm font-medium text-success-600">{{ metric.change }}</p>
        </article>
      </section>
      <section class="grid grid-cols-1 gap-6 xl:grid-cols-12">
        <div
          class="rounded-2xl border border-gray-200 bg-white p-5 shadow-theme-xs sm:p-6 xl:col-span-8"
        >
          <div class="flex items-start justify-between">
            <div>
              <h2 class="text-lg font-semibold text-gray-900">Ingresos mensuales</h2>
              <p class="mt-1 text-sm text-gray-500">Últimos siete meses</p>
            </div>
            <span class="rounded-full border border-gray-200 px-3 py-1 text-xs text-gray-500"
              >2026</span
            >
          </div>
          <VueApexCharts
            class="mt-4"
            type="area"
            height="280"
            :options="revenueOptions"
            :series="revenueSeries"
          />
        </div>
        <div
          class="rounded-2xl border border-gray-200 bg-white p-5 shadow-theme-xs sm:p-6 xl:col-span-4"
        >
          <h2 class="text-lg font-semibold text-gray-900">Asistencia semanal</h2>
          <p class="mt-1 text-sm text-gray-500">Entradas por día</p>
          <VueApexCharts
            class="mt-4"
            type="bar"
            height="280"
            :options="attendanceOptions"
            :series="attendanceSeries"
          />
        </div>
      </section>
      <section class="grid grid-cols-1 gap-6 xl:grid-cols-2">
        <div class="rounded-2xl border border-gray-200 bg-white p-5 shadow-theme-xs sm:p-6">
          <div class="flex items-center justify-between">
            <h2 class="text-lg font-semibold text-gray-900">Últimos accesos</h2>
            <RouterLink to="/qr" class="text-sm font-medium text-brand-600"
              >Ver control QR</RouterLink
            >
          </div>
          <div class="mt-5 divide-y divide-gray-100">
            <div
              v-for="access in accessLog"
              :key="access.id"
              class="flex items-center gap-3 py-3 first:pt-0"
            >
              <span
                :class="
                  access.status === 'OK'
                    ? 'bg-success-50 text-success-600'
                    : 'bg-error-50 text-error-600'
                "
                class="flex h-10 w-10 items-center justify-center rounded-full text-xs font-bold"
                >{{ access.status }}</span
              >
              <div class="min-w-0 flex-1">
                <p class="text-sm font-semibold text-gray-800">{{ access.member }}</p>
                <p class="text-xs text-gray-500">{{ access.type }} · {{ access.time }}</p>
              </div>
              <span
                :class="access.status === 'OK' ? 'bg-success-500' : 'bg-error-500'"
                class="h-2.5 w-2.5 rounded-full"
              ></span>
            </div>
          </div>
        </div>
        <div class="rounded-2xl border border-gray-200 bg-white p-5 shadow-theme-xs sm:p-6">
          <div class="flex items-center justify-between">
            <h2 class="text-lg font-semibold text-gray-900">Clientes por vencer</h2>
            <RouterLink to="/clientes" class="text-sm font-medium text-brand-600"
              >Ver clientes</RouterLink
            >
          </div>
          <div class="mt-5 space-y-4">
            <div v-for="member in expiringMembers" :key="member.id" class="flex items-center gap-3">
              <span
                class="flex h-9 w-9 items-center justify-center rounded-full bg-brand-50 text-xs font-semibold text-brand-600"
                >{{ member.initials }}</span
              >
              <div class="min-w-0 flex-1">
                <p class="truncate text-sm font-semibold text-gray-800">{{ member.name }}</p>
                <p class="text-xs text-gray-500">{{ member.plan }}</p>
              </div>
              <span
                class="rounded-full bg-warning-50 px-2.5 py-1 text-xs font-medium text-warning-600"
                >{{ member.status }}</span
              >
            </div>
          </div>
        </div>
      </section>
    </div>
  </AdminLayout>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import VueApexCharts from 'vue3-apexcharts'
import type { ApexOptions } from 'apexcharts'
import AdminLayout from '@/components/layout/AdminLayout.vue'
import { useGymData } from '@/composables/useGymData'
const { activeMembers, todaySessions, accessLog, members } = useGymData()
const metrics = computed(() => [
  {
    label: 'Clientes activos',
    value: activeMembers.value,
    change: '+12% vs mes anterior',
    icon: '◎',
    class: 'bg-success-50 text-success-600',
  },
  {
    label: 'Ingresos del mes',
    value: '$71,800',
    change: '+6.3% vs mes anterior',
    icon: '$',
    class: 'bg-blue-light-50 text-blue-light-600',
  },
  {
    label: 'Asistencias hoy',
    value: '47',
    change: '+8% vs ayer',
    icon: '↗',
    class: 'bg-orange-50 text-orange-600',
  },
  {
    label: 'Sesiones programadas',
    value: todaySessions.value,
    change: '2 pendientes de confirmar',
    icon: '◷',
    class: 'bg-brand-50 text-brand-600',
  },
])
const expiringMembers = computed(() =>
  members.value.filter((member) => member.status === 'Por vencer' || member.status === 'Vencido'),
)
const revenueSeries = [
  { name: 'Ingresos', data: [48500, 52000, 61500, 59000, 68000, 73500, 71800] },
]
const revenueOptions: ApexOptions = {
  chart: { toolbar: { show: false }, fontFamily: 'Outfit, sans-serif' },
  colors: ['#84cc16'],
  dataLabels: { enabled: false },
  stroke: { curve: 'smooth', width: 3 },
  fill: { type: 'gradient', gradient: { opacityFrom: 0.35, opacityTo: 0.03 } },
  grid: { borderColor: '#e4e7ec', strokeDashArray: 4 },
  xaxis: {
    categories: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul'],
    axisBorder: { show: false },
    axisTicks: { show: false },
  },
  yaxis: { labels: { formatter: (value: number) => `$${Math.round(value / 1000)}k` } },
  tooltip: { y: { formatter: (value: number) => `$${value.toLocaleString('en-US')}` } },
}
const attendanceSeries = [{ name: 'Entradas', data: [68, 82, 75, 96, 112, 124, 54] }]
const attendanceOptions: ApexOptions = {
  chart: { toolbar: { show: false }, fontFamily: 'Outfit, sans-serif' },
  colors: ['#f97316'],
  plotOptions: { bar: { borderRadius: 5, columnWidth: '42%' } },
  dataLabels: { enabled: false },
  grid: { borderColor: '#e4e7ec', strokeDashArray: 4 },
  xaxis: {
    categories: ['Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb', 'Dom'],
    axisBorder: { show: false },
    axisTicks: { show: false },
  },
  yaxis: { labels: { formatter: (value: number) => `${value}` } },
}
</script>
