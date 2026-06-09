import { useState, useEffect, useCallback } from 'react'
import type { ApplicationSource } from '../../types'
import { SOURCE_LABELS } from '../../types'

export interface Filters {
  search: string
  source: ApplicationSource | ''
  salaryMin: string
  salaryMax: string
}

const SOURCES: ApplicationSource[] = ['HH_RU', 'LINKEDIN', 'HABR_CAREER', 'COMPANY_WEBSITE', 'REFERRAL', 'OTHER']

interface Props {
  onChange: (filters: Filters) => void
}

const inputCls =
  'h-9 bg-white border border-gray-200 rounded-lg text-sm text-gray-800 outline-none ' +
  'transition-colors focus:border-indigo-400 focus:ring-2 focus:ring-indigo-100 placeholder:text-gray-400'

export default function FilterBar({ onChange }: Props) {
  const [filters, setFilters] = useState<Filters>({ search: '', source: '', salaryMin: '', salaryMax: '' })
  const [searchInput, setSearchInput] = useState('')

  useEffect(() => {
    const t = setTimeout(() => setFilters((f) => ({ ...f, search: searchInput })), 300)
    return () => clearTimeout(t)
  }, [searchInput])

  const update = useCallback((patch: Partial<Filters>) => {
    setFilters((f) => ({ ...f, ...patch }))
  }, [])

  useEffect(() => { onChange(filters) }, [filters, onChange])

  const hasFilters = searchInput || filters.source || filters.salaryMin || filters.salaryMax

  const reset = () => {
    setSearchInput('')
    setFilters({ search: '', source: '', salaryMin: '', salaryMax: '' })
  }

  return (
    <div className="mb-5 flex flex-wrap items-center gap-2">

      {/* Search */}
      <div className="relative">
        <svg className="absolute left-2.5 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400 pointer-events-none"
          viewBox="0 0 20 20" fill="currentColor">
          <path fillRule="evenodd" d="M9 3a6 6 0 100 12A6 6 0 009 3zM1 9a8 8 0 1114.32 4.906l3.387 3.387a1 1 0 01-1.414 1.414l-3.387-3.387A8 8 0 011 9z" clipRule="evenodd"/>
        </svg>
        <input
          type="text"
          placeholder="Search company or position…"
          value={searchInput}
          onChange={(e) => setSearchInput(e.target.value)}
          className={`${inputCls} pl-8 pr-3 w-60`}
        />
      </div>

      {/* Divider */}
      <div className="w-px h-5 bg-gray-200" />

      {/* Source */}
      <div className="relative">
        <svg className="absolute left-2.5 top-1/2 -translate-y-1/2 w-3.5 h-3.5 text-gray-400 pointer-events-none"
          viewBox="0 0 20 20" fill="currentColor">
          <path d="M2 5a2 2 0 012-2h12a2 2 0 012 2v2a2 2 0 01-2 2H4a2 2 0 01-2-2V5zM2 13a2 2 0 012-2h12a2 2 0 012 2v2a2 2 0 01-2 2H4a2 2 0 01-2-2v-2z"/>
        </svg>
        <select
          value={filters.source}
          onChange={(e) => update({ source: e.target.value as ApplicationSource | '' })}
          className={`${inputCls} pl-8 pr-7 appearance-none cursor-pointer`}
          style={{ backgroundImage: "url(\"data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='12' height='12' viewBox='0 0 20 20' fill='%239ca3af'%3E%3Cpath fill-rule='evenodd' d='M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z'/%3E%3C/svg%3E\")", backgroundRepeat: 'no-repeat', backgroundPosition: 'right 10px center' }}
        >
          <option value="">All Sources</option>
          {SOURCES.map((s) => (
            <option key={s} value={s}>{SOURCE_LABELS[s]}</option>
          ))}
        </select>
      </div>

      {/* Divider */}
      <div className="w-px h-5 bg-gray-200" />

      {/* Salary range */}
      <div className="flex items-center gap-1.5">
        <svg className="w-3.5 h-3.5 text-gray-400 shrink-0" viewBox="0 0 20 20" fill="currentColor">
          <path d="M8.433 7.418c.155-.103.346-.196.567-.267v1.698a2.305 2.305 0 01-.567-.267C8.07 8.34 8 8.114 8 8c0-.114.07-.34.433-.582zM11 12.849v-1.698c.22.071.412.164.567.267.364.243.433.468.433.582 0 .114-.07.34-.433.582a2.305 2.305 0 01-.567.267z"/>
          <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm1-13a1 1 0 10-2 0v.092a4.535 4.535 0 00-1.676.662C6.602 6.234 6 7.009 6 8c0 .99.602 1.765 1.324 2.246.48.32 1.054.545 1.676.662v1.941c-.391-.127-.68-.317-.843-.504a1 1 0 10-1.51 1.31c.562.649 1.413 1.028 2.353 1.118V15a1 1 0 102 0v-.092a4.535 4.535 0 001.676-.662C13.398 13.766 14 12.991 14 12c0-.99-.602-1.765-1.324-2.246A4.535 4.535 0 0011 9.092V7.151c.391.127.68.317.843.504a1 1 0 101.511-1.31c-.563-.649-1.413-1.028-2.354-1.118V5z" clipRule="evenodd"/>
        </svg>
        <input
          type="number"
          placeholder="Min salary"
          value={filters.salaryMin}
          onChange={(e) => update({ salaryMin: e.target.value })}
          className={`${inputCls} px-2.5 w-28`}
        />
        <span className="text-gray-300 font-light">—</span>
        <input
          type="number"
          placeholder="Max"
          value={filters.salaryMax}
          onChange={(e) => update({ salaryMax: e.target.value })}
          className={`${inputCls} px-2.5 w-20`}
        />
      </div>

      {/* Clear */}
      {hasFilters && (
        <button
          onClick={reset}
          className="h-9 px-3 flex items-center gap-1.5 text-sm text-gray-500 hover:text-red-500 rounded-lg hover:bg-red-50 transition-colors cursor-pointer"
        >
          <svg className="w-3.5 h-3.5" viewBox="0 0 20 20" fill="currentColor">
            <path fillRule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clipRule="evenodd"/>
          </svg>
          Clear
        </button>
      )}
    </div>
  )
}
