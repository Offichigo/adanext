import { useEffect, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import { organizationApi, projectApi } from '../services/api'
import AppLayout from '../components/layout/AppLayout'
import CreateProjectModal from '../components/project/CreateProjectModal'
import styles from './OrganizationDetailPage.module.css'

export default function OrganizationDetailPage() {
  const { id } = useParams()
  const [organization, setOrganization] = useState(null)
  const [projects, setProjects] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [showModal, setShowModal] = useState(false)

  useEffect(() => {
    loadData()
  }, [id])

  async function loadData() {
    try {
      const [org, projs] = await Promise.all([
        organizationApi.findById(id),
        projectApi.findAll(id),
      ])
      setOrganization(org)
      setProjects(projs)
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  function handleProjectCreated(newProject) {
    setProjects((prev) => [...prev, newProject])
    setShowModal(false)
  }

  if (loading) return <AppLayout><p className={styles.stateMsg}>Chargement...</p></AppLayout>
  if (error) return <AppLayout><p className={styles.errorMsg} role="alert">{error}</p></AppLayout>

  return (
    <AppLayout>
      <nav className={styles.breadcrumb} aria-label="Fil d'ariane">
        <Link to="/organizations" className={styles.breadcrumbLink}>
          Organisations
        </Link>
        <span className={styles.breadcrumbSep} aria-hidden="true">/</span>
        <span>{organization.name}</span>
      </nav>

      <div className={styles.header}>
        <div>
          <p className={styles.orgLabel}>Organisation</p>
          <h1 className={styles.orgName}>{organization.name}</h1>
          {organization.description && (
            <p className={styles.orgDesc}>{organization.description}</p>
          )}
        </div>
        <button
          className={styles.createButton}
          onClick={() => setShowModal(true)}
          type="button"
        >
          Nouveau projet
        </button>
      </div>

      <section aria-labelledby="projects-heading">
        <h2 id="projects-heading" className={styles.sectionTitle}>
          Projets
        </h2>

        {projects.length === 0 && (
          <div className={styles.empty}>
            <p>Cette organisation n'a pas encore de projet.</p>
          </div>
        )}

        <ul className={styles.projectGrid} aria-label="Liste des projets">
          {projects.map((project) => (
            <li key={project.id}>
              <Link
                to={`/organizations/${id}/projects/${project.id}`}
                className={styles.projectCard}
              >
                <span className={styles.projectLabel}>Projet</span>
                <h3 className={styles.projectName}>{project.name}</h3>
                {project.description && (
                  <p className={styles.projectDesc}>{project.description}</p>
                )}
                <span
                  className={styles.projectStatus}
                  data-status={project.status}
                >
                  {project.status === 'ACTIVE' ? 'Actif' : 'Archive'}
                </span>
              </Link>
            </li>
          ))}
        </ul>
      </section>

      {showModal && (
        <CreateProjectModal
          organizationId={id}
          onCreated={handleProjectCreated}
          onClose={() => setShowModal(false)}
        />
      )}
    </AppLayout>
  )
}
