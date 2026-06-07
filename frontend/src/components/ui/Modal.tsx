import type { ReactNode } from 'react'

interface Props {
  open: boolean
  title: string
  onClose: () => void
  children: ReactNode
}

export default function Modal({ open, title, onClose, children }: Props) {
  if (!open) return null
  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40" onClick={onClose}>
      <div
        className="w-full max-w-md rounded-xl bg-white p-6 shadow-xl"
        onClick={(e) => e.stopPropagation()}
      >
        <div className="mb-4 flex items-center justify-between">
          <h2 className="text-lg font-semibold text-gray-900">{title}</h2>
          <button onClick={onClose} className="text-gray-400 hover:text-gray-600 text-xl leading-none cursor-pointer">×</button>
        </div>
        {children}
      </div>
    </div>
  )
}
