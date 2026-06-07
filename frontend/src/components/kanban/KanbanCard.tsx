import { useSortable } from '@dnd-kit/sortable'
import { CSS } from '@dnd-kit/utilities'
import { useNavigate } from 'react-router-dom'
import type { JobApplication } from '../../types'
import { SOURCE_LABELS } from '../../types'

interface Props {
  application: JobApplication
}

export default function KanbanCard({ application }: Props) {
  const navigate = useNavigate()
  const { attributes, listeners, setNodeRef, transform, transition, isDragging } = useSortable({
    id: application.id,
  })

  const style = {
    transform: CSS.Transform.toString(transform),
    transition,
    opacity: isDragging ? 0.5 : 1,
  }

  return (
    <div
      ref={setNodeRef}
      style={style}
      {...attributes}
      {...listeners}
      className="rounded-lg bg-white border border-gray-200 p-3 shadow-sm cursor-grab active:cursor-grabbing select-none"
      onClick={() => navigate(`/applications/${application.id}`)}
    >
      <p className="font-semibold text-gray-900 text-sm truncate">{application.companyName}</p>
      <p className="text-xs text-gray-500 mt-0.5 truncate">{application.position}</p>
      <div className="mt-2 flex items-center justify-between text-xs text-gray-400">
        <span>{SOURCE_LABELS[application.source]}</span>
        {(application.salaryMin || application.salaryMax) && (
          <span className="text-gray-600 font-medium">
            {application.salaryMin && `${application.salaryMin.toLocaleString()}`}
            {application.salaryMin && application.salaryMax && ' – '}
            {application.salaryMax && `${application.salaryMax.toLocaleString()} ${application.currency}`}
          </span>
        )}
      </div>
      <p className="mt-1 text-xs text-gray-400">{new Date(application.appliedAt).toLocaleDateString('en-GB')}</p>
    </div>
  )
}
