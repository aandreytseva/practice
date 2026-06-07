package org.practice.exception

class ResourceNotFoundException(message: String) : RuntimeException(message)
class ConflictException(message: String) : RuntimeException(message)
class ForbiddenException(message: String) : RuntimeException(message)
class InvalidStatusTransitionException(message: String) : RuntimeException(message)
