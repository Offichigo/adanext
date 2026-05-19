import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { organizationApi } from '../services/api'
import AppLayout from '../components/layout/AppLayout'
import CreateOrganizationModal from '../components/organization/CreateOrganizationModal'
import styles from './OrganizationsPage.module.css'

export default function OrganizationsPage() {
  const [organizations, setOrganizations] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [showModal, setShowModal] = useState(false)

  useEffect(() => {
    loadOrganizations()
  }, [])

  async function loadOrganizations() {
    try {
      const data = await organizationApi.findAll()
      setOrganizations(data)
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  function handleCreated(newOrg) {
    setOrganizations((prev) => [...prev, newOrg])
    setShowModal(false)
  }

  return (
    <AppLayout>
      <div className={styles.header}>
        <div>
          <h1 className={styles.pageTitle}>Mes organisations</h1>
          <p className={styles.pageDesc}>
            Chaque organisation regroupe des projets et des membres.
          </p>
        </div>
        <button
          className={styles.createButton}
          onClick={() => setShowModal(true)}
          type="button"
        >
          Nouvelle organisation
        </button>
      </div>

      {loading && <p className={styles.stateMsg}>Chargement...</p>}
      {error && (
        <p className={styles.errorMsg} role="alert">
          {error}
        </p>
      )}

      {!loading && !error && organizations.length === 0 && (
        <div className={styles.empty}>
          <p>Vous n'avez pas encore d'organisation.</p>
          <button
            className={styles.createButton}
            onClick={() => setShowModal(true)}
            type="button"
          >
            Creer ma premiere organisation
          </button>
        </div>
      )}

      <ul className={styles.grid} aria-label="Liste des organisations">
        {organizations.map((org) => (
          <li key={org.id}>
            <Link to={`/organizations/${org.id}`} className={styles.card}>
              <span className={styles.cardLabel}>Organisation</span>
              <h2 className={styles.cardName}>{org.name}</h2>
              {org.description && (
                <p className={styles.cardDesc}>{org.description}</p>
              )}
            </Link>
          </li>
        ))}
      </ul>

      {showModal && (
        <CreateOrganizationModal
          onCreated={handleCreated}
          onClose={() => setShowModal(false)}
        />
      )}
    </AppLayout>
  )
}
