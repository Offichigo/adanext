import { Link, useNavigate } from 'react-router-dom'
import { authApi } from '../../services/api'
import styles from './Navbar.module.css'

export default function Navbar() {
  const navigate = useNavigate()

  async function handleLogout() {
    try {
      await authApi.logout()
      navigate('/login')
    } catch {
      navigate('/login')
    }
  }

  return (
    <nav className={styles.navbar} aria-label="Navigation principale">
      <div className={styles.brand}>
        <Link to="/organizations" className={styles.brandLink}>
          Ada<span className={styles.brandAccent}>Next</span>
        </Link>
      </div>
      <button
        className={styles.logoutButton}
        onClick={handleLogout}
        type="button"
      >
        Deconnexion
      </button>
    </nav>
  )
}
