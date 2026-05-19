import { useEffect, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import { projectApi, taskApi } from '../services/api'
import AppLayout from '../components/layout/AppLayout'
import KanbanColumn from '../components/kanban/KanbanColumn'
import AddTaskForm from '../components/kanban/AddTaskForm'
import styles from './KanbanPage.module.css'

const COLUMNS = [
  { status: 'TODO', label: 'A faire' },
  { status: 'IN_PROGRESS', label: 'En cours' },
  { status: 'DONE', label: 'Termine' },
]

export default function KanbanPage() {
  const { organizationId, projectId } = useParams()
  const [project, setProject] = useState(null)
  const [tasks, setTasks] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [showAddForm, setShowAddForm] = useState(false)

  useEffect(() => {
    loadData()
  }, [projectId])

  async function loadData() {
    try {
      const [proj, allTasks] = await Promise.all([
        projectApi.findById(organizationId, projectId),
        taskApi.findAll(projectId),
      ])
      setProject(proj)
      setTasks(allTasks)
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  async function handleStatusChange(taskId, newStatus) {
    try {
      const updated = await taskApi.updateStatus(projectId, taskId, newStatus)
      setTasks((prev) =>
        prev.map((t) => (t.id === taskId ? updated : t))
      )
    } catch (err) {
      setError(err.message)
    }
  }

  function handleTaskCreated(newTask) {
    setTasks((prev) => [...prev, newTask])
    setShowAddForm(false)
  }

  const tasksByStatus = (status) => tasks.filter((t) => t.status === status)

  if (loading) return <AppLayout><p className={styles.stateMsg}>Chargement...</p></AppLayout>
  if (error) return <AppLayout><p className={styles.errorMsg} role="alert">{error}</p></AppLayout>

  return (
    <AppLayout>
      <nav className={styles.breadcrumb} aria-label="Fil d'ariane">
        <Link to="/organizations" className={styles.breadcrumbLink}>
          Organisations
        </Link>
        <span className={styles.breadcrumbSep} aria-hidden="true">/</span>
        <Link
          to={`/organizations/${organizationId}`}
          className={styles.breadcrumbLink}
        >
          Organisation
        </Link>
        <span className={styles.breadcrumbSep} aria-hidden="true">/</span>
        <span>{project.name}</span>
      </nav>

      <div className={styles.header}>
        <div>
          <p className={styles.projectLabel}>Kanban</p>
          <h1 className={styles.projectName}>{project.name}</h1>
          {project.description && (
            <p className={styles.projectDesc}>{project.description}</p>
          )}
        </div>
        <button
          className={styles.addButton}
          onClick={() => setShowAddForm(true)}
          type="button"
        >
          Ajouter une tache
        </button>
      </div>

      <div className={styles.board} role="region" aria-label="Tableau kanban">
        {COLUMNS.map((col) => (
          <KanbanColumn
            key={col.status}
            status={col.status}
            label={col.label}
            tasks={tasksByStatus(col.status)}
            allStatuses={COLUMNS}
            onStatusChange={handleStatusChange}
          />
        ))}
      </div>

      {showAddForm && (
        <AddTaskForm
          projectId={projectId}
          onCreated={handleTaskCreated}
          onClose={() => setShowAddForm(false)}
        />
      )}
    </AppLayout>
  )
}
