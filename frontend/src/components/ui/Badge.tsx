import type { ApplicationStatus } from '../../types'
import { STATUS_COLORS, STATUS_LABELS } from '../../types'

export default function Badge({ status }: { status: ApplicationStatus }) {
  return (
    <span className={`inline-block rounded-full px-2.5 py-0.5 text-xs font-medium ${STATUS_COLORS[status]}`}>
      {STATUS_LABELS[status]}
    </span>
  )
}
