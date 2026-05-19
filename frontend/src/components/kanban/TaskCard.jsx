import styles from './TaskCard.module.css'

export default function TaskCard({ task, allStatuses, onStatusChange }) {
  const otherStatuses = allStatuses.filter((s) => s.status !== task.status)

  return (
    <article className={styles.card} aria-label={`Tache : ${task.title}`}>
      <h3 className={styles.title}>{task.title}</h3>
      {task.description && (
        <p className={styles.desc}>{task.description}</p>
      )}

      <div className={styles.actions}>
        {otherStatuses.map((target) => (
          <button
            key={target.status}
            className={styles.moveButton}
            onClick={() => onStatusChange(task.id, target.status)}
            type="button"
            aria-label={`Deplacer vers ${target.label}`}
          >
            {target.label}
          </button>
        ))}
      </div>
    </article>
  )
}
