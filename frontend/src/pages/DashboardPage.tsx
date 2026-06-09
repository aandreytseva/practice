import { useEffect, useState, useCallback, useMemo } from 'react'
import { useAuth } from '../context/AuthContext'
import { applicationsApi, type CreateApplicationPayload } from '../api/applications'
import type { JobApplication, StatsResponse, ApplicationSource } from '../types'
import { ALL_STATUSES, STATUS_LABELS, SOURCE_LABELS } from '../types'
import KanbanBoard from '../components/kanban/KanbanBoard'
import Modal from '../components/ui/Modal'
import Button from '../components/ui/Button'
import Input from '../components/ui/Input'
import FilterBar, { type Filters } from '../components/ui/FilterBar'

const SOURCES: ApplicationSource[] = ['HH_RU', 'LINKEDIN', 'HABR_CAREER', 'COMPANY_WEBSITE', 'REFERRAL', 'OTHER']

export default function DashboardPage() {
  const { user, logout } = useAuth()
  const [applications, setApplications] = useState<JobApplication[]>([])
  const [stats, setStats] = useState<StatsResponse | null>(null)
  const [loading, setLoading] = useState(true)
  const [showModal, setShowModal] = useState(false)
  const [saving, setSaving] = useState(false)
  const [filters, setFilters] = useState<Filters>({ search: '', source: '', salaryMin: '', salaryMax: '' })
  const [form, setForm] = useState<CreateApplicationPayload>({
    companyName: '',
    position: '',
    source: 'OTHER',
    jobUrl: '',
    salaryMin: undefined,
    salaryMax: undefined,
    currency: 'EUR',
  })

  const fetchAll = async () => {
    setLoading(true)
    try {
      const [page, s] = await Promise.all([
        applicationsApi.getAll({ size: 200 }),
        applicationsApi.getStats(),
      ])
      setApplications(page.content)
      setStats(s)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { fetchAll() }, [])

  // Client-side filtering
  const filtered = useMemo(() => {
    return applications.filter((app) => {
      if (filters.search) {
        const q = filters.search.toLowerCase()
        if (!app.companyName.toLowerCase().includes(q) && !app.position.toLowerCase().includes(q)) return false
      }
      if (filters.source && app.source !== filters.source) return false
      if (filters.salaryMin && (app.salaryMin ?? 0) < Number(filters.salaryMin)) return false
      if (filters.salaryMax && (app.salaryMax ?? Infinity) > Number(filters.salaryMax)) return false
      return true
    })
  }, [applications, filters])

  const handleFiltersChange = useCallback((f: Filters) => setFilters(f), [])

  const handleCreate = async () => {
    if (!form.companyName || !form.position) return
    setSaving(true)
    try {
      const app = await applicationsApi.create(form)
      setApplications((prev) => [app, ...prev])
      setStats((prev) => prev ? {
        total: prev.total + 1,
        byStatus: { ...prev.byStatus, APPLIED: (prev.byStatus.APPLIED ?? 0) + 1 }
      } : prev)
      setShowModal(false)
      setForm({ companyName: '', position: '', source: 'OTHER', jobUrl: '', currency: 'EUR' })
    } finally {
      setSaving(false)
    }
  }

  const handleUpdate = (updated: JobApplication) => {
    setApplications((prev) => prev.map((a) => a.id === updated.id ? updated : a))
  }

  const isFiltering = filters.search || filters.source || filters.salaryMin || filters.salaryMax

  return (
    <div className="min-h-screen bg-gray-100">
      {/* Navbar */}
      <header className="bg-white border-b border-gray-200 px-6 py-3 flex items-center justify-between">
        <h1 className="text-lg font-bold text-indigo-700">Job Tracker</h1>
        <div className="flex items-center gap-4">
          <span className="text-sm text-gray-600">{user?.fullName}</span>
          <Button variant="ghost" size="sm" onClick={logout}>Sign Out</Button>
        </div>
      </header>

      <main className="p-6 max-w-full">
        {/* Stats */}
        {stats && (
          <div className="mb-6 grid grid-cols-2 sm:grid-cols-4 lg:grid-cols-7 gap-3">
            {ALL_STATUSES.map((s) => (
              <div key={s} className="bg-white rounded-xl border border-gray-200 p-3 text-center">
                <p className="text-2xl font-bold text-gray-900">{stats.byStatus[s] ?? 0}</p>
                <p className="text-xs text-gray-500 mt-0.5">{STATUS_LABELS[s]}</p>
              </div>
            ))}
          </div>
        )}

        {/* Header row */}
        <div className="mb-3 flex items-center justify-between">
          <h2 className="text-base font-semibold text-gray-800">
            {isFiltering
              ? `${filtered.length} of ${applications.length} shown`
              : `Total: ${stats?.total ?? '—'}`}
          </h2>
          <Button onClick={() => setShowModal(true)}>+ Add Application</Button>
        </div>

        {/* Filter bar */}
        <FilterBar onChange={handleFiltersChange} />

        {/* Kanban */}
        {loading ? (
          <div className="text-center py-20 text-gray-400">Loading...</div>
        ) : (
          <KanbanBoard applications={filtered} onUpdate={handleUpdate} />
        )}
      </main>

      {/* Create modal */}
      <Modal open={showModal} title="New Application" onClose={() => setShowModal(false)}>
        <div className="flex flex-col gap-3">
          <Input
            label="Company *"
            value={form.companyName}
            onChange={(e) => setForm((f) => ({ ...f, companyName: e.target.value }))}
            placeholder="Endava"
          />
          <Input
            label="Position *"
            value={form.position}
            onChange={(e) => setForm((f) => ({ ...f, position: e.target.value }))}
            placeholder="Backend Developer"
          />
          <Input
            label="Job URL"
            value={form.jobUrl ?? ''}
            onChange={(e) => setForm((f) => ({ ...f, jobUrl: e.target.value }))}
            placeholder="https://..."
          />
          <div className="grid grid-cols-2 gap-2">
            <Input
              label="Salary from"
              type="number"
              value={form.salaryMin ?? ''}
              onChange={(e) => setForm((f) => ({ ...f, salaryMin: e.target.value ? +e.target.value : undefined }))}
              placeholder="2000"
            />
            <Input
              label="to"
              type="number"
              value={form.salaryMax ?? ''}
              onChange={(e) => setForm((f) => ({ ...f, salaryMax: e.target.value ? +e.target.value : undefined }))}
              placeholder="3500"
            />
          </div>
          <div className="flex flex-col gap-1">
            <label className="text-sm font-medium text-gray-700">Currency</label>
            <select
              className="rounded-lg border border-gray-300 px-3 py-2 text-sm outline-none focus:border-indigo-500"
              value={form.currency}
              onChange={(e) => setForm((f) => ({ ...f, currency: e.target.value }))}
            >
              <option value="EUR">EUR</option>
              <option value="USD">USD</option>
              <option value="MDL">MDL</option>
              <option value="RON">RON</option>
            </select>
          </div>
          <div className="flex flex-col gap-1">
            <label className="text-sm font-medium text-gray-700">Source</label>
            <select
              className="rounded-lg border border-gray-300 px-3 py-2 text-sm outline-none focus:border-indigo-500"
              value={form.source}
              onChange={(e) => setForm((f) => ({ ...f, source: e.target.value as ApplicationSource }))}
            >
              {SOURCES.map((s) => (
                <option key={s} value={s}>{SOURCE_LABELS[s]}</option>
              ))}
            </select>
          </div>
          <div className="mt-2 flex justify-end gap-2">
            <Button variant="secondary" onClick={() => setShowModal(false)}>Cancel</Button>
            <Button onClick={handleCreate} loading={saving} disabled={!form.companyName || !form.position}>
              Create
            </Button>
          </div>
        </div>
      </Modal>
    </div>
  )
}
