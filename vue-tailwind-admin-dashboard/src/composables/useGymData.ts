import { computed, ref } from 'vue'
import { api } from '@/services/api'

export type EstadoGeneral = 'Activo' | 'Inactivo'
export type SessionStatus = 'Programada' | 'Confirmada' | 'Completada' | 'Cancelada'
export type Genero = 'Masculino' | 'Femenino'
export type TipoPlan = 'MENSUAL' | 'ANUAL'

// Datos de persona (tabla `persona`)
export interface Member {
  id: string
  nombres: string
  apellidos: string
  genero: Genero
  fechaNacimiento: string
  direccion: string
  email: string
  telefono: string
  dui: string
  planId: string | null
  estado: EstadoGeneral
  notas: string
  codigoQr: string
  creadoEn: string
}

export interface GymPlan {
  id: string
  nombre: string
  detalles: string
  precio: number
  tipoPlan: TipoPlan
  duracionMeses: number
  estado: EstadoGeneral
}

export type DatosCliente = Omit<Member, 'id' | 'codigoQr' | 'creadoEn'>
export type DatosPlan = Omit<GymPlan, 'id' | 'duracionMeses'>

export interface CambioFechaSuscripcion {
  idSuscripcion: number
  nombrePlan: string
  fechaInicioActual: string
  fechaVencimientoActual: string
  nuevaFechaInicio: string
  nuevaFechaVencimiento: string
}

export interface VistaCancelacion {
  cambios: CambioFechaSuscripcion[]
  quedaSinPlanVigente: boolean
}

export interface Suscripcion {
  idSuscripcion: number
  nombrePlan: string
  tipoPlan: TipoPlan
  fechaInicio: string
  fechaVencimiento: string
  precioPagado: number
  nombreEstado: string
  vigente: boolean
}

export interface Trainer {
  id: string
  nombres: string
  apellidos: string
  genero: Genero
  fechaNacimiento: string
  direccion: string
  email: string
  telefono: string
  dui: string
  nombreUsuario: string
  especialidad: string
  tarifaPorSesion: number
  estado: EstadoGeneral
}

export interface GymSession {
  id: string
  clienteId: string
  entrenadorId: string
  fecha: string
  horaInicio: string
  horaFin: string
  estado: SessionStatus
}

export interface AsistenciaRecord {
  id: string
  clienteId: string
  nombreCliente: string
  estado: string
  fecha: string
  hora: string
  horaSalida: string
}

export type TipoMovimiento = 'ENTRADA' | 'SALIDA' | 'RECHAZADO' | 'DUPLICADO'

export interface AccesoQr {
  tipoMovimiento: TipoMovimiento
  permitido: boolean
  mensaje: string
  nombreCliente: string
  nombrePlan: string | null
  fechaVencimiento: string | null
  horaEntrada: string | null
  horaSalida: string | null
}

// Un intento rechazado se guarda con estado "Cancelada" y no cuenta como asistencia
export const esIngreso = (record: AsistenciaRecord) => record.estado !== 'Cancelada'

export const etiquetaAsistencia = (estado: string) =>
  ({ Activo: 'Dentro', Completada: 'Salió', Cancelada: 'Rechazado' })[estado] ?? estado

export const nombreCompleto = (persona: { nombres: string; apellidos: string }) =>
  `${persona.nombres} ${persona.apellidos}`

export const iniciales = (persona: { nombres: string; apellidos: string }) =>
  `${persona.nombres.charAt(0)}${persona.apellidos.charAt(0)}`.toUpperCase()

const initialTrainers: Trainer[] = [
  {
    id: '1',
    nombres: 'Nelson Eduardo',
    apellidos: 'Palacios Henríquez',
    genero: 'Masculino',
    fechaNacimiento: '1991-02-03',
    direccion: 'Urbanización Prados de Venecia, Soyapango',
    email: 'nelson.trainer@gmail.com',
    telefono: '7745-9988',
    dui: '04891234-5',
    nombreUsuario: 'nelson_coach',
    especialidad: 'Culturismo, HIIT y Nutrición Funcional',
    tarifaPorSesion: 15,
    estado: 'Activo',
  },
]

const initialSessions: GymSession[] = [
  {
    id: '1',
    clienteId: '1',
    entrenadorId: '1',
    fecha: '2026-09-23',
    horaInicio: '08:00',
    horaFin: '09:00',
    estado: 'Completada',
  },
  {
    id: '2',
    clienteId: '2',
    entrenadorId: '1',
    fecha: '2026-09-26',
    horaInicio: '16:00',
    horaFin: '17:00',
    estado: 'Programada',
  },
]


interface ClienteApi {
  idCliente: number
  nombres: string
  apellidos: string
  genero: Genero
  fechaNacimiento: string
  direccion: string | null
  email: string
  telefono: string
  dui: string
  idPlanSuscripcion: number | null
  nombreEstado: string
  notas: string | null
  codigoQr: string
  creadoEn: string | null
}

interface PlanApi {
  idPlanSuscripcion: number
  nombreSuscripcion: string
  detalles: string | null
  precio: number
  tipoPlan: TipoPlan
  duracionMeses: number
  nombreEstado: string
}

interface AsistenciaApi {
  idAsistencia: number
  idCliente: number
  nombreCliente: string
  nombreEstado: string
  fechaAsistencia: string
  horaSesion: string
  horaSalida: string | null
}

const aAsistencia = (a: AsistenciaApi): AsistenciaRecord => ({
  id: String(a.idAsistencia),
  clienteId: String(a.idCliente),
  nombreCliente: a.nombreCliente,
  estado: a.nombreEstado,
  fecha: a.fechaAsistencia,
  hora: a.horaSesion.slice(0, 5),
  horaSalida: a.horaSalida ? a.horaSalida.slice(0, 5) : '',
})

interface EstadoApi {
  idEstado: number
  nombreEstado: string
}

const aEstadoGeneral = (nombre: string): EstadoGeneral => (nombre === 'Activo' ? 'Activo' : 'Inactivo')

const aMember = (c: ClienteApi): Member => ({
  id: String(c.idCliente),
  nombres: c.nombres,
  apellidos: c.apellidos,
  genero: c.genero,
  fechaNacimiento: c.fechaNacimiento,
  direccion: c.direccion ?? '',
  email: c.email,
  telefono: c.telefono,
  dui: c.dui,
  planId: c.idPlanSuscripcion == null ? null : String(c.idPlanSuscripcion),
  estado: aEstadoGeneral(c.nombreEstado),
  notas: c.notas ?? '',
  codigoQr: c.codigoQr,
  creadoEn: (c.creadoEn ?? '').slice(0, 10),
})

const aPlan = (p: PlanApi): GymPlan => ({
  id: String(p.idPlanSuscripcion),
  nombre: p.nombreSuscripcion,
  detalles: p.detalles ?? '',
  precio: p.precio,
  tipoPlan: p.tipoPlan,
  duracionMeses: p.duracionMeses,
  estado: aEstadoGeneral(p.nombreEstado),
})

const members = ref<Member[]>([])
const trainers = ref<Trainer[]>(initialTrainers)
const plans = ref<GymPlan[]>([])
const estados = ref<EstadoApi[]>([])
const errorCarga = ref('')
let datosCargados = false
const sessions = ref<GymSession[]>(initialSessions)
const asistencia = ref<AsistenciaRecord[]>([])

export function useGymData() {
  const activeMembers = computed(
    () => members.value.filter((member) => member.estado === 'Activo').length,
  )
  const today = computed(() => sessions.value[sessions.value.length - 1]?.fecha || '')
  const todaySessions = computed(
    () => sessions.value.filter((session) => session.fecha === today.value).length,
  )

  const memberById = (id: string) => members.value.find((member) => member.id === id)
  const trainerById = (id: string) => trainers.value.find((trainer) => trainer.id === id)
  const planById = (id: string | null) => plans.value.find((plan) => plan.id === id)
  const attendanceByMember = (id: string) =>
    asistencia.value.filter((record) => record.clienteId === id)
  const ingresosDeCliente = (id: string) => attendanceByMember(id).filter(esIngreso).length

  const idEstado = (nombre: string) => {
    const estado = estados.value.find((item) => item.nombreEstado === nombre)
    if (!estado) throw new Error(`No existe el estado ${nombre}`)
    return estado.idEstado
  }

  const cargarClientes = async () => {
    members.value = (await api.get<ClienteApi[]>('/clientes')).map(aMember)
  }
  // Del mas reciente al mas antiguo
  const cargarAsistencias = async () => {
    asistencia.value = (await api.get<AsistenciaApi[]>('/asistencias'))
      .map(aAsistencia)
      .sort((a, b) => `${b.fecha} ${b.hora}`.localeCompare(`${a.fecha} ${a.hora}`))
  }
  const cargarPlanes = async () => {
    plans.value = (await api.get<PlanApi[]>('/planes-suscripcion')).map(aPlan)
  }
  // Se carga una sola vez al entrar; las altas y cambios recargan solo su lista
  const cargarDatos = async () => {
    if (datosCargados) return
    try {
      const [listaEstados] = await Promise.all([
        api.get<EstadoApi[]>('/estados'),
        cargarClientes(),
        cargarPlanes(),
        cargarAsistencias(),
      ])
      estados.value = listaEstados
      datosCargados = true
      errorCarga.value = ''
    } catch (e) {
      errorCarga.value = e instanceof Error ? e.message : 'No se pudieron cargar los datos'
    }
  }

  const clienteRequest = (datos: DatosCliente) => ({
    nombres: datos.nombres,
    apellidos: datos.apellidos,
    genero: datos.genero,
    fechaNacimiento: datos.fechaNacimiento,
    direccion: datos.direccion,
    email: datos.email,
    telefono: datos.telefono,
    dui: datos.dui,
    idPlanSuscripcion: datos.planId ? Number(datos.planId) : null,
    idEstado: idEstado(datos.estado),
    notas: datos.notas,
  })
  const addMember = async (datos: DatosCliente) => {
    await api.post('/clientes', clienteRequest(datos))
    await cargarClientes()
  }
  const updateMember = async (id: string, cambios: Partial<DatosCliente>) => {
    const actual = memberById(id)
    if (!actual) return
    await api.put(`/clientes/${id}`, clienteRequest({ ...actual, ...cambios }))
    await cargarClientes()
  }

  const planRequest = (datos: DatosPlan) => ({
    nombreSuscripcion: datos.nombre,
    detalles: datos.detalles,
    precio: datos.precio,
    tipoPlan: datos.tipoPlan,
    idEstado: idEstado(datos.estado),
  })
  const addPlan = async (datos: DatosPlan) => {
    await api.post('/planes-suscripcion', planRequest(datos))
    await cargarPlanes()
  }
  const updatePlan = async (id: string, datos: DatosPlan) => {
    await api.put(`/planes-suscripcion/${id}`, planRequest(datos))
    await cargarPlanes()
  }

  const suscripcionesDeCliente = (idCliente: string) =>
    api.get<Suscripcion[]>(`/suscripciones/cliente/${idCliente}`)
  // El backend recalcula el plan actual del cliente al vender o cancelar
  const venderSuscripcion = async (idCliente: string, planId: string) => {
    await api.post('/suscripciones', {
      idCliente: Number(idCliente),
      idPlanSuscripcion: Number(planId),
    })
    await cargarClientes()
  }
  const vistaCancelacion = (idSuscripcion: number) =>
    api.get<VistaCancelacion>(`/suscripciones/${idSuscripcion}/cancelacion`)
  const cancelarSuscripcion = async (idSuscripcion: number) => {
    await api.put(`/suscripciones/${idSuscripcion}/cancelar`)
    await cargarClientes()
  }

  const addTrainer = (trainer: Trainer) => trainers.value.unshift(trainer)
  const updateTrainer = (id: string, changes: Partial<Trainer>) => {
    const trainer = trainers.value.find((item) => item.id === id)
    if (trainer) Object.assign(trainer, changes)
  }

  const addSession = (session: GymSession) => sessions.value.push(session)
  const updateSessionStatus = (id: string, estado: SessionStatus) => {
    const session = sessions.value.find((item) => item.id === id)
    if (session) session.estado = estado
  }
  const deleteSession = (id: string) => {
    sessions.value = sessions.value.filter((session) => session.id !== id)
  }

  // El backend decide si es entrada, salida, duplicado o rechazado
  const registrarAccesoQr = async (codigoQr: string) => {
    const resultado = await api.post<AccesoQr>('/asistencias/qr', { codigoQr })
    await cargarAsistencias()
    return resultado
  }
  const asistenciasDeHoy = async () =>
    (await api.get<AsistenciaApi[]>('/asistencias/hoy')).map(aAsistencia)
  const presentesAhora = async () =>
    (await api.get<AsistenciaApi[]>('/asistencias/presentes')).map(aAsistencia)

  return {
    members,
    trainers,
    plans,
    sessions,
    asistencia,
    activeMembers,
    todaySessions,
    memberById,
    trainerById,
    planById,
    attendanceByMember,
    ingresosDeCliente,
    errorCarga,
    cargarDatos,
    addMember,
    updateMember,
    addPlan,
    updatePlan,
    vistaCancelacion,
    suscripcionesDeCliente,
    venderSuscripcion,
    cancelarSuscripcion,
    addTrainer,
    updateTrainer,
    addSession,
    updateSessionStatus,
    deleteSession,
    registrarAccesoQr,
    asistenciasDeHoy,
    presentesAhora,
  }
}
