<template>
  <AdminLayout
    ><div class="space-y-6">
      <header>
        <p class="text-sm font-medium text-brand-600">Check-in rápido</p>
        <h1 class="mt-1 text-3xl font-semibold text-gray-900">Recepción</h1>
      </header>
      <div class="grid grid-cols-1 gap-6 lg:grid-cols-2">
        <section class="rounded-2xl border border-gray-200 bg-white p-6 shadow-theme-xs">
          <h2 class="text-lg font-semibold text-gray-900">Registrar ingreso</h2>
          <input
            v-model="query"
            class="mt-5 h-12 w-full rounded-xl border border-gray-200 px-4 text-sm outline-none focus:border-brand-500"
            placeholder="Buscar cliente por nombre..."
          />
          <div v-if="selected" class="mt-5 rounded-xl bg-brand-50 p-4">
            <div class="flex items-center gap-3">
              <span
                class="flex h-12 w-12 items-center justify-center rounded-full bg-white font-semibold text-brand-600"
                >{{ selected.initials }}</span
              >
              <div>
                <p class="font-semibold text-gray-800">{{ selected.name }}</p>
                <p class="text-sm text-gray-500">{{ selected.plan }} · {{ selected.status }}</p>
              </div>
            </div>
            <button
              class="mt-4 w-full rounded-lg bg-brand-500 py-2.5 text-sm font-medium text-white"
              @click="checkIn"
            >
              Registrar Ingreso
            </button>
          </div>
          <div v-else-if="query" class="mt-3 space-y-2">
            <button
              v-for="member in suggestions"
              :key="member.id"
              class="flex w-full items-center gap-3 rounded-lg border border-gray-100 p-3 text-start hover:bg-gray-50"
              @click="selected = member"
            >
              <span
                class="flex h-9 w-9 items-center justify-center rounded-full bg-brand-50 text-xs font-semibold text-brand-600"
                >{{ member.initials }}</span
              ><span class="text-sm font-medium text-gray-800">{{ member.name }}</span>
            </button>
          </div>
        </section>
        <section class="rounded-2xl border border-gray-200 bg-white p-6 shadow-theme-xs">
          <div class="flex items-center justify-between">
            <div>
              <p class="text-sm text-gray-500">Presentes ahora</p>
              <p class="mt-1 text-4xl font-semibold text-gray-900">{{ presentMembers.length }}</p>
            </div>
            <span class="rounded-full bg-success-50 px-3 py-1 text-sm font-medium text-success-600"
              >En el gimnasio</span
            >
          </div>
          <div class="mt-6 divide-y divide-gray-100">
            <div
              v-for="member in presentMembers"
              :key="member.id"
              class="flex items-center gap-3 py-3"
            >
              <span
                class="flex h-9 w-9 items-center justify-center rounded-full bg-success-50 text-xs font-semibold text-success-600"
                >{{ member.initials }}</span
              >
              <div>
                <p class="text-sm font-semibold text-gray-800">{{ member.name }}</p>
                <p class="text-xs text-gray-500">Ingreso registrado hoy</p>
              </div>
            </div>
          </div>
        </section>
      </div>
    </div></AdminLayout
  >
</template>
<script setup lang="ts">
import { computed, ref } from 'vue'
import AdminLayout from '@/components/layout/AdminLayout.vue'
import { useGymData, type Member } from '@/composables/useGymData'
const { members, presentMembers, registerAccess } = useGymData()
const query = ref('')
const selected = ref<Member | null>(null)
const suggestions = computed(() =>
  members.value.filter(
    (m) => m.name.toLowerCase().includes(query.value.toLowerCase()) && query.value,
  ),
)
const checkIn = () => {
  if (selected.value) registerAccess(selected.value)
  query.value = ''
  selected.value = null
}
</script>
