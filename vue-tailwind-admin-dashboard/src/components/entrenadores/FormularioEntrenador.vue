<template>
  <Modal full-screen-backdrop @close="emit('close')">
    <template #body>
      <div class="p-4">
        <form
          class="max-h-[90vh] w-full max-w-140 overflow-y-auto rounded-2xl bg-white shadow-theme-xl"
          @submit.prevent="guardar"
        >
          <header class="flex items-center justify-between border-b border-gray-200 p-6">
            <h2 class="text-xl font-semibold text-gray-900">
              {{ entrenador ? `Editar — ${nombreCompleto(entrenador)}` : 'Nuevo Entrenador' }}
            </h2>
            <button type="button" class="text-2xl text-gray-400" @click="emit('close')">×</button>
          </header>
          <div class="grid grid-cols-1 gap-4 p-6 sm:grid-cols-2">
            <label :class="etiqueta"
              >Nombres<input v-model="form.nombres" required :class="campo"
            /></label>
            <label :class="etiqueta"
              >Apellidos<input v-model="form.apellidos" required :class="campo"
            /></label>
            <label :class="etiqueta"
              >Género<select v-model="form.genero" :class="campo">
                <option value="Masculino">Masculino</option>
                <option value="Femenino">Femenino</option>
              </select></label
            >
            <label :class="etiqueta"
              >DUI<input
                v-model="form.dui"
                required
                pattern="\d{8}-\d"
                maxlength="10"
                title="Formato 00000000-0"
                placeholder="04891234-5"
                :class="campo"
            /></label>
            <label :class="etiqueta"
              >Email<input v-model="form.email" required type="email" :class="campo"
            /></label>
            <label :class="etiqueta"
              >Teléfono<input
                v-model="form.telefono"
                required
                pattern="\d{4}-\d{4}"
                maxlength="9"
                title="Formato 0000-0000"
                placeholder="7745-9988"
                :class="campo"
            /></label>
            <label :class="etiqueta"
              >Fecha de nacimiento<input
                v-model="form.fechaNacimiento"
                required
                type="date"
                :class="campo"
            /></label>
            <label :class="etiqueta"
              >{{ entrenador ? 'Nueva contraseña (opcional)' : 'Contraseña'
              }}<input
                v-model="form.contrasena"
                :required="!entrenador"
                type="password"
                :placeholder="entrenador ? 'Dejar vacío para no cambiarla' : ''"
                :class="campo"
            /></label>
            <label :class="[etiqueta, 'sm:col-span-2']"
              >Dirección<input v-model="form.direccion" :class="campo"
            /></label>
            <label :class="[etiqueta, 'sm:col-span-2']"
              >Especialidad<input
                v-model="form.especialidad"
                required
                placeholder="Culturismo, HIIT y Nutrición Funcional"
                :class="campo"
            /></label>
            <label :class="etiqueta"
              >Tarifa por sesión ($)<input
                v-model.number="form.tarifaPorSesion"
                required
                min="0"
                step="0.01"
                type="number"
                :class="campo"
            /></label>
            <label v-if="entrenador" :class="etiqueta"
              >Estado<select v-model="form.estado" :class="campo">
                <option value="Activo">Activo</option>
                <option value="Inactivo">Inactivo</option>
              </select></label
            >
            <p v-if="!entrenador" class="text-xs text-gray-500 sm:col-span-2">
              El usuario para iniciar sesión será el DUI.
            </p>
          </div>
          <div v-if="error" class="px-6 pb-2">
            <Alert variant="error" title="No se pudo guardar" :message="error" />
          </div>
          <footer class="flex gap-3 border-t border-gray-100 p-6">
            <Button
              type="button"
              variant="outline"
              class-name="flex-1 justify-center"
              @click="emit('close')"
              >Cancelar</Button
            >
            <Button type="submit" :disabled="guardando" class-name="flex-1 justify-center">{{
              guardando ? 'Guardando...' : entrenador ? 'Guardar cambios' : 'Registrar entrenador'
            }}</Button>
          </footer>
        </form>
      </div>
    </template>
  </Modal>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import Alert from '@/components/ui/Alert.vue'
import Button from '@/components/ui/Button.vue'
import Modal from '@/components/ui/Modal.vue'
import {
  useGymData,
  nombreCompleto,
  type DatosEntrenador,
  type Trainer,
} from '@/composables/useGymData'

// Sin entrenador = crear; con entrenador = editar
const props = defineProps<{ entrenador?: Trainer | null }>()
const emit = defineEmits<{ close: []; guardado: [] }>()
const { addTrainer, updateTrainer } = useGymData()

const etiqueta = 'text-xs font-semibold uppercase tracking-wider text-gray-500'
const campo = 'mt-2 h-11 w-full rounded-xl border border-gray-200 bg-gray-50 px-3 text-sm'

const e = props.entrenador
const form = reactive<DatosEntrenador>({
  nombres: e?.nombres ?? '',
  apellidos: e?.apellidos ?? '',
  genero: e?.genero ?? 'Masculino',
  dui: e?.dui ?? '',
  email: e?.email ?? '',
  telefono: e?.telefono ?? '',
  fechaNacimiento: e?.fechaNacimiento ?? '',
  direccion: e?.direccion ?? '',
  especialidad: e?.especialidad ?? '',
  tarifaPorSesion: e?.tarifaPorSesion ?? 0,
  estado: e?.estado ?? 'Activo',
  contrasena: '',
})
const error = ref('')
const guardando = ref(false)

const guardar = async () => {
  guardando.value = true
  error.value = ''
  try {
    const datos = { ...form, tarifaPorSesion: Number(form.tarifaPorSesion) }
    if (props.entrenador) await updateTrainer(props.entrenador.id, datos)
    else await addTrainer(datos)
    emit('guardado')
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'No se pudo guardar el entrenador.'
  } finally {
    guardando.value = false
  }
}
</script>
