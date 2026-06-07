import { useDroppable } from '@dnd-kit/core'
import { SortableContext, verticalListSortingStrategy } from '@dnd-kit/sortable'
import type { JobApplication, ApplicationStatus } from '../../types'
import { STATUS_LABELS } from '../../types'
import KanbanCard from './KanbanCard'

const COLUMN_COLORS: Record<ApplicationStatus, string> = {
  APPLIED:         'border-t-blue-500',
  HR_CALL:         'border-t-purple-500',
  TECH_INTERVIEW:  'border-t-orange-500',
  FINAL_INTERVIEW: 'border-t-yellow-500',
  OFFER:           'border-t-green-500',
  REJECTED:        'border-t-red-400',
  WITHDRAWN:       'border-t-gray-400',
}

interface Props {
  status: ApplicationStatus
  applications: JobApplication[]
}

export default function KanbanColumn({ status, applications }: Props) {
  const { setNodeRef, isOver } = useDroppable({ id: status })

  return (
    <div className={`flex flex-col rounded-xl border-t-4 bg-gray-50 ${COLUMN_COLORS[status]} flex-1 min-w-[160px]`}>
      <div className="px-3 py-2 flex items-center justify-between">
        <span className="text-xs font-semibold text-gray-700 uppercase tracking-wide">
          {STATUS_LABELS[status]}
        </span>
        <span className="text-xs bg-gray-200 text-gray-600 rounded-full px-2 py-0.5">{applications.length}</span>
      </div>
      <SortableContext items={applications.map((a) => a.id)} strategy={verticalListSortingStrategy}>
        <div
          ref={setNodeRef}
          className={`flex flex-col gap-2 p-2 min-h-[120px] flex-1 rounded-b-xl transition-colors ${isOver ? 'bg-indigo-50' : ''}`}
        >
          {applications.map((app) => (
            <KanbanCard key={app.id} application={app} />
          ))}
        </div>
      </SortableContext>
    </div>
  )
}
