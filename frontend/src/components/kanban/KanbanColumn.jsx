import TaskCard from './TaskCard'
import styles from './KanbanColumn.module.css'

const STATUS_CONFIG = {
  TODO: { color: 'var(--color-gris-txt)', label: 'A faire' },
  IN_PROGRESS: { color: 'var(--color-violet)', label: 'En cours' },
  DONE: { color: 'var(--color-vert)', label: 'Termine' },
}

export default function KanbanColumn({ status, label, tasks, allStatuses, onStatusChange }) {
  const config = STATUS_CONFIG[status]

  return (
    <section
      className={styles.column}
      aria-labelledby={`col-${status}`}
    >
      <header
        className={styles.columnHeader}
        style={{ borderTopColor: config.color }}
      >
        <h2 id={`col-${status}`} className={styles.columnTitle}>
          {label}
        </h2>
        <span className={styles.count} aria-label={`${tasks.length} tache${tasks.length > 1 ? 's' : ''}`}>
          {tasks.length}
        </span>
      </header>

      <ul className={styles.taskList} aria-label={`Taches : ${label}`}>
        {tasks.length === 0 && (
          <li className={styles.empty} aria-live="polite">
            Aucune tache
          </li>
        )}
        {tasks.map((task) => (
          <li key={task.id}>
            <TaskCard
              task={task}
              allStatuses={allStatuses}
              onStatusChange={onStatusChange}
            />
          </li>
        ))}
      </ul>
    </section>
  )
}
