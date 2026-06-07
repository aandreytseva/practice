import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { applicationsApi } from '../api/applications'
import { notesApi } from '../api/notes'
import { remindersApi } from '../api/reminders'
import type { JobApplication, Note, Reminder, StatusHistoryEntry, ApplicationStatus } from '../types'
import { STATUS_LABELS, STATUS_TRANSITIONS, SOURCE_LABELS } from '../types'
import Badge from '../components/ui/Badge'
import Button from '../components/ui/Button'
import Modal from '../components/ui/Modal'
import Input from '../components/ui/Input'

type Tab = 'notes' | 'reminders' | 'history'

export default function ApplicationDetailPage() {
  const { id } = useParams<{ id: string }>()
  const navigate = useNavigate()
  const appId = Number(id)

  const [app, setApp] = useState<JobApplication | null>(null)
  const [notes, setNotes] = useState<Note[]>([])
  const [reminders, setReminders] = useState<Reminder[]>([])
  const [history, setHistory] = useState<StatusHistoryEntry[]>([])
  const [tab, setTab] = useState<Tab>('notes')
  const [loading, setLoading] = useState(true)

  // Note state
  const [noteText, setNoteText] = useState('')
  const [editingNote, setEditingNote] = useState<Note | null>(null)
  const [savingNote, setSavingNote] = useState(false)

  // Reminder state
  const [showReminderModal, setShowReminderModal] = useState(false)
  const [reminderTitle, setReminderTitle] = useState('')
  const [reminderAt, setReminderAt] = useState('')
  const [savingReminder, setSavingReminder] = useState(false)

  // Status change
  const [showStatusModal, setShowStatusModal] = useState(false)
  const [targetStatus, setTargetStatus] = useState<ApplicationStatus | ''>('')
  const [statusComment, setStatusComment] = useState('')
  const [savingStatus, setSavingStatus] = useState(false)

  // Delete
  const [showDeleteModal, setShowDeleteModal] = useState(false)

  useEffect(() => {
    const load = async () => {
      setLoading(true)
      try {
        const [a, n, r, h] = await Promise.all([
          applicationsApi.getById(appId),
          notesApi.getAll(appId),
          remindersApi.getAll(appId),
          applicationsApi.getHistory(appId),
        ])
        setApp(a); setNotes(n); setReminders(r); setHistory(h)
      } finally {
        setLoading(false)
      }
    }
    load()
  }, [appId])

  /* ── Notes ── */
  const handleSaveNote = async () => {
    if (!noteText.trim()) return
    setSavingNote(true)
    try {
      if (editingNote) {
        const updated = await notesApi.update(appId, editingNote.id, noteText)
        setNotes((prev) => prev.map((n) => n.id === editingNote.id ? updated : n))
        setEditingNote(null)
      } else {
        const created = await notesApi.create(appId, noteText)
        setNotes((prev) => [created, ...prev])
      }
      setNoteText('')
    } finally {
      setSavingNote(false)
    }
  }

  const handleDeleteNote = async (noteId: number) => {
    await notesApi.delete(appId, noteId)
    setNotes((prev) => prev.filter((n) => n.id !== noteId))
  }

  /* ── Reminders ── */
  const handleCreateReminder = async () => {
    if (!reminderTitle || !reminderAt) return
    setSavingReminder(true)
    try {
      const r = await remindersApi.create(appId, reminderTitle, new Date(reminderAt).toISOString())
      setReminders((prev) => [...prev, r])
      setShowReminderModal(false)
      setReminderTitle(''); setReminderAt('')
    } finally {
      setSavingReminder(false)
    }
  }

  const handleMarkDone = async (reminderId: number) => {
    const updated = await remindersApi.markDone(appId, reminderId)
    setReminders((prev) => prev.map((r) => r.id === reminderId ? updated : r))
  }

  const handleDeleteReminder = async (reminderId: number) => {
    await remindersApi.delete(appId, reminderId)
    setReminders((prev) => prev.filter((r) => r.id !== reminderId))
  }

  /* ── Status ── */
  const handleChangeStatus = async () => {
    if (!targetStatus || !app) return
    setSavingStatus(true)
    try {
      const updated = await applicationsApi.updateStatus(appId, targetStatus, statusComment || undefined)
      setApp(updated)
      const h = await applicationsApi.getHistory(appId)
      setHistory(h)
      setShowStatusModal(false)
      setTargetStatus(''); setStatusComment('')
    } finally {
      setSavingStatus(false)
    }
  }

  /* ── Delete ── */
  const handleDelete = async () => {
    await applicationsApi.delete(appId)
    navigate('/')
  }

  if (loading || !app) {
    return <div className="min-h-screen flex items-center justify-center text-gray-400">Loading...</div>
  }

  const allowedNext = STATUS_TRANSITIONS[app.status]

  return (
    <div className="min-h-screen bg-gray-100">
      <header className="bg-white border-b border-gray-200 px-6 py-3 flex items-center gap-4">
        <button onClick={() => navigate('/')} className="text-gray-400 hover:text-gray-700 cursor-pointer text-xl">←</button>
        <div className="flex-1">
          <h1 className="text-lg font-bold text-gray-900">{app.companyName}</h1>
          <p className="text-sm text-gray-500">{app.position}</p>
        </div>
        <Badge status={app.status} />
        {allowedNext.length > 0 && (
          <Button size="sm" onClick={() => setShowStatusModal(true)}>Change Status</Button>
        )}
        <Button size="sm" variant="danger" onClick={() => setShowDeleteModal(true)}>Delete</Button>
      </header>

      <main className="max-w-4xl mx-auto p-6 grid grid-cols-1 md:grid-cols-3 gap-6">
        {/* Info card */}
        <div className="md:col-span-1 bg-white rounded-xl border border-gray-200 p-4 flex flex-col gap-3 h-fit">
          <h2 className="font-semibold text-gray-800 text-sm uppercase tracking-wide">Details</h2>
          <InfoRow label="Source" value={SOURCE_LABELS[app.source]} />
          <InfoRow label="Applied" value={new Date(app.appliedAt).toLocaleDateString('en-US')} />
          {(app.salaryMin || app.salaryMax) && (
            <InfoRow
              label="Salary"
              value={`${app.salaryMin?.toLocaleString() ?? '?'} – ${app.salaryMax?.toLocaleString() ?? '?'} ${app.currency}`}
            />
          )}
          {app.jobUrl && (
            <a href={app.jobUrl} target="_blank" rel="noopener noreferrer" className="text-sm text-indigo-600 hover:underline break-all">
              View Job Posting ↗
            </a>
          )}
        </div>

        {/* Tabs */}
        <div className="md:col-span-2 flex flex-col gap-4">
          <div className="flex gap-1 bg-white rounded-xl border border-gray-200 p-1">
            {(['notes', 'reminders', 'history'] as Tab[]).map((t) => (
              <button
                key={t}
                onClick={() => setTab(t)}
                className={`flex-1 rounded-lg py-1.5 text-sm font-medium transition-colors cursor-pointer ${tab === t ? 'bg-indigo-600 text-white' : 'text-gray-600 hover:bg-gray-100'}`}
              >
                {t === 'notes' ? `Notes (${notes.length})` : t === 'reminders' ? `Reminders (${reminders.length})` : 'History'}
              </button>
            ))}
          </div>

          {/* Notes */}
          {tab === 'notes' && (
            <div className="flex flex-col gap-3">
              <div className="bg-white rounded-xl border border-gray-200 p-4 flex flex-col gap-2">
                <textarea
                  className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm resize-none outline-none focus:border-indigo-500 focus:ring-2 focus:ring-indigo-100"
                  rows={3}
                  placeholder="Add a note..."
                  value={noteText}
                  onChange={(e) => setNoteText(e.target.value)}
                />
                <div className="flex gap-2 justify-end">
                  {editingNote && (
                    <Button variant="secondary" size="sm" onClick={() => { setEditingNote(null); setNoteText('') }}>Cancel</Button>
                  )}
                  <Button size="sm" loading={savingNote} onClick={handleSaveNote} disabled={!noteText.trim()}>
                    {editingNote ? 'Save' : 'Add'}
                  </Button>
                </div>
              </div>
              {notes.map((note) => (
                <div key={note.id} className="bg-white rounded-xl border border-gray-200 p-4">
                  <p className="text-sm text-gray-800 whitespace-pre-wrap">{note.content}</p>
                  <div className="mt-2 flex items-center justify-between text-xs text-gray-400">
                    <span>{new Date(note.updatedAt).toLocaleString('en-US')}</span>
                    <div className="flex gap-2">
                      <button onClick={() => { setEditingNote(note); setNoteText(note.content) }} className="hover:text-indigo-600 cursor-pointer">Edit</button>
                      <button onClick={() => handleDeleteNote(note.id)} className="hover:text-red-500 cursor-pointer">Delete</button>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}

          {/* Reminders */}
          {tab === 'reminders' && (
            <div className="flex flex-col gap-3">
              <div className="flex justify-end">
                <Button size="sm" onClick={() => setShowReminderModal(true)}>+ Add Reminder</Button>
              </div>
              {reminders.length === 0 && <p className="text-sm text-gray-400 text-center py-6">No reminders yet</p>}
              {reminders.map((r) => (
                <div key={r.id} className={`bg-white rounded-xl border p-4 flex items-start justify-between gap-3 ${r.isDone ? 'opacity-50' : 'border-gray-200'}`}>
                  <div>
                    <p className={`text-sm font-medium ${r.isDone ? 'line-through text-gray-400' : 'text-gray-800'}`}>{r.title}</p>
                    <p className="text-xs text-gray-400 mt-0.5">{new Date(r.remindAt).toLocaleString('en-US')}</p>
                  </div>
                  <div className="flex gap-2 shrink-0">
                    {!r.isDone && (
                      <Button size="sm" variant="secondary" onClick={() => handleMarkDone(r.id)}>✓</Button>
                    )}
                    <Button size="sm" variant="danger" onClick={() => handleDeleteReminder(r.id)}>×</Button>
                  </div>
                </div>
              ))}
            </div>
          )}

          {/* History */}
          {tab === 'history' && (
            <div className="bg-white rounded-xl border border-gray-200 p-4">
              {history.length === 0 && <p className="text-sm text-gray-400">No history yet</p>}
              <div className="flex flex-col gap-4">
                {history.map((entry, i) => (
                  <div key={entry.id} className="flex gap-3">
                    <div className="flex flex-col items-center">
                      <div className="w-3 h-3 rounded-full bg-indigo-500 mt-1 shrink-0" />
                      {i < history.length - 1 && <div className="w-0.5 flex-1 bg-gray-200 mt-1" />}
                    </div>
                    <div className="pb-2">
                      <p className="text-sm text-gray-800">
                        {entry.fromStatus ? (
                          <><span className="font-medium">{STATUS_LABELS[entry.fromStatus]}</span> → <span className="font-medium">{STATUS_LABELS[entry.toStatus]}</span></>
                        ) : (
                          <span className="font-medium">Created: {STATUS_LABELS[entry.toStatus]}</span>
                        )}
                      </p>
                      {entry.comment && <p className="text-xs text-gray-500 mt-0.5">{entry.comment}</p>}
                      <p className="text-xs text-gray-400 mt-0.5">{new Date(entry.changedAt).toLocaleString('en-US')}</p>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          )}
        </div>
      </main>

      {/* Status modal */}
      <Modal open={showStatusModal} title="Change Status" onClose={() => setShowStatusModal(false)}>
        <div className="flex flex-col gap-3">
          <div className="flex flex-col gap-1">
            <label className="text-sm font-medium text-gray-700">New Status</label>
            <select
              className="rounded-lg border border-gray-300 px-3 py-2 text-sm outline-none focus:border-indigo-500"
              value={targetStatus}
              onChange={(e) => setTargetStatus(e.target.value as ApplicationStatus)}
            >
              <option value="">— select —</option>
              {allowedNext.map((s) => (
                <option key={s} value={s}>{STATUS_LABELS[s]}</option>
              ))}
            </select>
          </div>
          <Input
            label="Comment (optional)"
            value={statusComment}
            onChange={(e) => setStatusComment(e.target.value)}
            placeholder="How did it go?"
          />
          <div className="flex justify-end gap-2 mt-1">
            <Button variant="secondary" onClick={() => setShowStatusModal(false)}>Cancel</Button>
            <Button onClick={handleChangeStatus} loading={savingStatus} disabled={!targetStatus}>Save</Button>
          </div>
        </div>
      </Modal>

      {/* Reminder modal */}
      <Modal open={showReminderModal} title="New Reminder" onClose={() => setShowReminderModal(false)}>
        <div className="flex flex-col gap-3">
          <Input label="Title" value={reminderTitle} onChange={(e) => setReminderTitle(e.target.value)} placeholder="Call recruiter" />
          <Input label="Date & Time" type="datetime-local" value={reminderAt} onChange={(e) => setReminderAt(e.target.value)} />
          <div className="flex justify-end gap-2 mt-1">
            <Button variant="secondary" onClick={() => setShowReminderModal(false)}>Cancel</Button>
            <Button onClick={handleCreateReminder} loading={savingReminder} disabled={!reminderTitle || !reminderAt}>Create</Button>
          </div>
        </div>
      </Modal>

      {/* Delete modal */}
      <Modal open={showDeleteModal} title="Delete Application?" onClose={() => setShowDeleteModal(false)}>
        <p className="text-sm text-gray-600 mb-4">This action is irreversible. All notes and reminders will be deleted.</p>
        <div className="flex justify-end gap-2">
          <Button variant="secondary" onClick={() => setShowDeleteModal(false)}>Cancel</Button>
          <Button variant="danger" onClick={handleDelete}>Delete</Button>
        </div>
      </Modal>
    </div>
  )
}

function InfoRow({ label, value }: { label: string; value: string }) {
  return (
    <div>
      <p className="text-xs text-gray-400">{label}</p>
      <p className="text-sm text-gray-800 font-medium">{value}</p>
    </div>
  )
}
