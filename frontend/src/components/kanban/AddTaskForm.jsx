import { useState } from 'react'
import { taskApi } from '../../services/api'
import styles from '../organization/CreateOrganizationModal.module.css'

export default function AddTaskForm({ projectId, onCreated, onClose }) {
  const [form, setForm] = useState({ title: '', description: '' })
  const [error, setError] = useState(null)
  const [loading, setLoading] = useState(false)

  function handleChange(e) {
    setForm((prev) => ({ ...prev, [e.target.name]: e.target.value }))
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setError(null)
    setLoading(true)
    try {
      const created = await taskApi.create(projectId, form)
      onCreated(created)
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className={styles.overlay} role="dialog" aria-modal="true" aria-labelledby="modal-title">
      <div className={styles.modal}>
        <div className={styles.modalHeader}>
          <h2 id="modal-title" className={styles.modalTitle}>
            Nouvelle tache
          </h2>
          <button
            className={styles.closeButton}
            onClick={onClose}
            type="button"
            aria-label="Fermer"
          >
            x
          </button>
        </div>

        <form className={styles.form} onSubmit={handleSubmit}>
          {error && (
            <p className={styles.error} role="alert">
              {error}
            </p>
          )}

          <div className={styles.field}>
            <label htmlFor="task-title" className={styles.label}>
              Titre
            </label>
            <input
              id="task-title"
              name="title"
              type="text"
              required
              className={styles.input}
              value={form.title}
              onChange={handleChange}
              autoFocus
            />
          </div>

          <div className={styles.field}>
            <label htmlFor="task-description" className={styles.label}>
              Description
              <span className={styles.optional}>(facultatif)</span>
            </label>
            <textarea
              id="task-description"
              name="description"
              className={styles.textarea}
              value={form.description}
              onChange={handleChange}
              rows={3}
            />
          </div>

          <div className={styles.actions}>
            <button
              type="button"
              className={styles.cancelButton}
              onClick={onClose}
            >
              Annuler
            </button>
            <button
              type="submit"
              className={styles.submitButton}
              disabled={loading}
            >
              {loading ? 'Ajout...' : 'Ajouter'}
            </button>
          </div>
        </form>
      </div>
    </div>
  )
}
