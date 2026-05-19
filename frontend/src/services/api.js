const BASE_URL = '/api'

async function request(path, options = {}) {
  const response = await fetch(`${BASE_URL}${path}`, {
    credentials: 'include',
    headers: {
      'Content-Type': 'application/json',
      ...options.headers,
    },
    ...options,
  })

  if (!response.ok) {
    const error = await response.json().catch(() => ({ message: 'Erreur reseau' }))
    throw new Error(error.message || 'Une erreur est survenue')
  }

  const text = await response.text()
  return text ? JSON.parse(text) : null
}

export const authApi = {
  register: (data) =>
    request('/auth/register', { method: 'POST', body: JSON.stringify(data) }),

  login: (data) => {
    const params = new URLSearchParams()
    params.append('email', data.email)
    params.append('password', data.password)
    return fetch(`${BASE_URL}/auth/login`, {
      method: 'POST',
      credentials: 'include',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: params,
    }).then((res) => {
      if (!res.ok) throw new Error('Email ou mot de passe incorrect')
      return res.json()
    })
  },

  logout: () => request('/auth/logout', { method: 'POST' }),
}

export const organizationApi = {
  create: (data) =>
    request('/organizations', { method: 'POST', body: JSON.stringify(data) }),

  findAll: () => request('/organizations'),

  findById: (id) => request(`/organizations/${id}`),
}

export const projectApi = {
  create: (organizationId, data) =>
    request(`/organizations/${organizationId}/projects`, {
      method: 'POST',
      body: JSON.stringify(data),
    }),

  findAll: (organizationId) =>
    request(`/organizations/${organizationId}/projects`),

  findById: (organizationId, projectId) =>
    request(`/organizations/${organizationId}/projects/${projectId}`),
}

export const taskApi = {
  create: (projectId, data) =>
    request(`/projects/${projectId}/tasks`, {
      method: 'POST',
      body: JSON.stringify(data),
    }),

  findAll: (projectId) => request(`/projects/${projectId}/tasks`),

  updateStatus: (projectId, taskId, status) =>
    request(`/projects/${projectId}/tasks/${taskId}/status`, {
      method: 'PATCH',
      body: JSON.stringify({ status }),
    }),
}
