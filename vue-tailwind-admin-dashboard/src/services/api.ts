// Direccion del backend: se lee del archivo .env; si no existe, usa la local
const API_URL: string = import.meta.env.VITE_API_URL ?? 'http://localhost:8080/api'

// Error que devuelve la API
export class ApiError extends Error {
  readonly estado: number

  constructor(estado: number, mensaje: string) {
    super(mensaje)
    this.name = 'ApiError'
    this.estado = estado
  }
}

// Mensajes para las respuestas de error que llegan sin cuerpo
const MENSAJES_POR_ESTADO: Record<number, string> = {
  401: 'Usuario o contraseña incorrectos',
  404: 'El registro solicitado no existe',
}

async function solicitar<T>(metodo: string, ruta: string, cuerpo?: unknown): Promise<T> {
  let respuesta: Response
  try {
    respuesta = await fetch(`${API_URL}${ruta}`, {
      method: metodo,
      headers: { 'Content-Type': 'application/json' },
      body: cuerpo === undefined ? undefined : JSON.stringify(cuerpo),
    })
  } catch {
    //Backend apagado o sin red
    throw new ApiError(0, 'No se pudo conectar con el servidor. Verifique que el backend esté encendido.')
  }

  if (!respuesta.ok) {
    let mensaje = MENSAJES_POR_ESTADO[respuesta.status] ?? `Error ${respuesta.status}`
    try {
      const error = await respuesta.json()
      if (error?.mensaje) mensaje = error.mensaje // ErrorResponse del ManejadorGlobalExcepciones
    } catch {
      // la respuesta no trae cuerpo JSON: se queda el mensaje por defecto
    }
    throw new ApiError(respuesta.status, mensaje)
  }

  // 204 No Content si no hay cuerpo que leer
  if (respuesta.status === 204) {
    return undefined as T
  }
  return (await respuesta.json()) as T
}

export const api = {
  get: <T>(ruta: string) => solicitar<T>('GET', ruta),
  post: <T>(ruta: string, cuerpo?: unknown) => solicitar<T>('POST', ruta, cuerpo),
  put: <T>(ruta: string, cuerpo?: unknown) => solicitar<T>('PUT', ruta, cuerpo),
  delete: (ruta: string) => solicitar<void>('DELETE', ruta),
}
