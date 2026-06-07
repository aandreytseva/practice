import { useState } from 'react'
import {
  DndContext,
  DragOverlay,
  PointerSensor,
  useSensor,
  useSensors,
  type DragStartEvent,
  type DragEndEvent,
} from '@dnd-kit/core'
import type { JobApplication, ApplicationStatus } from '../../types'
import { ALL_STATUSES, STATUS_TRANSITIONS } from '../../types'
import { applicationsApi } from '../../api/applications'
import KanbanColumn from './KanbanColumn'
import KanbanCard from './KanbanCard'

interface Props {
  applications: JobApplication[]
  onUpdate: (updated: JobApplication) => void
}

export default function KanbanBoard({ applications, onUpdate }: Props) {
  const [activeApp, setActiveApp] = useState<JobApplication | null>(null)

  const sensors = useSensors(
    useSensor(PointerSensor, { activationConstraint: { distance: 5 } })
  )

  const grouped = ALL_STATUSES.reduce<Record<ApplicationStatus, JobApplication[]>>((acc, status) => {
    acc[status] = applications.filter((a) => a.status === status)
    return acc
  }, {} as Record<ApplicationStatus, JobApplication[]>)

  const handleDragStart = (event: DragStartEvent) => {
    const app = applications.find((a) => a.id === event.active.id)
    if (app) setActiveApp(app)
  }

  const handleDragEnd = async (event: DragEndEvent) => {
    setActiveApp(null)
    const { active, over } = event
    if (!over || !activeApp) return

    // over.id is either a column status (string) or a card id (number)
    let targetStatus: ApplicationStatus
    if (typeof over.id === 'string') {
      targetStatus = over.id as ApplicationStatus
    } else {
      // dropped onto another card — find which column it belongs to
      const targetApp = applications.find((a) => a.id === over.id)
      if (!targetApp) return
      targetStatus = targetApp.status
    }

    if (targetStatus === activeApp.status) return

    const allowed = STATUS_TRANSITIONS[activeApp.status]
    if (!allowed.includes(targetStatus)) return // silently ignore invalid transitions

    try {
      const updated = await applicationsApi.updateStatus(activeApp.id, targetStatus)
      onUpdate(updated)
    } catch {
      alert('Failed to update status')
    }
  }

  return (
    <DndContext sensors={sensors} onDragStart={handleDragStart} onDragEnd={handleDragEnd}>
      <div className="flex gap-3 pb-4">
        {ALL_STATUSES.map((status) => (
          <KanbanColumn key={status} status={status} applications={grouped[status]} />
        ))}
      </div>
      <DragOverlay>
        {activeApp && (
          <div className="rotate-2 scale-105">
            <KanbanCard application={activeApp} />
          </div>
        )}
      </DragOverlay>
    </DndContext>
  )
}
