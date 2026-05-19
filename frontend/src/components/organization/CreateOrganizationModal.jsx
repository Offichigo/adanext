import { useState } from 'react'
import { organizationApi } from '../../services/api'
import styles from './CreateOrganizationModal.module.css'

export default function CreateOrganizationModal({ onCreated, onClose }) {
  const [form, setForm] = useState({ name: '', description: '' })
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
      const created = await organizationApi.create(form)
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
            Nouvelle organisation
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
            <label htmlFor="org-name" className={styles.label}>
              Nom
            </label>
            <input
              id="org-name"
              name="name"
              type="text"
              required
              className={styles.input}
              value={form.name}
              onChange={handleChange}
              autoFocus
            />
          </div>

          <div className={styles.field}>
            <label htmlFor="org-description" className={styles.label}>
              Description
              <span className={styles.optional}>(facultatif)</span>
            </label>
            <textarea
              id="org-description"
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
              {loading ? 'Creation...' : 'Creer'}
            </button>
          </div>
        </form>
      </div>
    </div>
  )
}
