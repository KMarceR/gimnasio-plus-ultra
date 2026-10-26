<template>
  <div class="relative mt-2">
    <input
      v-model="valor"
      :type="visible ? 'text' : 'password'"
      :required="required"
      :placeholder="placeholder"
      autocomplete="new-password"
      class="h-11 w-full rounded-xl border border-gray-200 bg-gray-50 ps-3 pe-10 text-sm"
    />
    <button
      type="button"
      :aria-label="visible ? 'Ocultar contraseña' : 'Mostrar contraseña'"
      class="absolute end-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600"
      @click="visible = !visible"
    >
      <EyeOffIcon v-if="visible" :size="16" />
      <EyeIcon v-else :size="16" />
    </button>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { Eye as EyeIcon, EyeOff as EyeOffIcon } from 'lucide-vue-next'

defineProps<{ required?: boolean; placeholder?: string }>()
const valor = defineModel<string>({ required: true })
const visible = ref(false)
</script>
