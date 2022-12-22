package cz.majksa.trask.entrance

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
class Candidate(
    var name: String,
    var surname: String,
    var email: String,
    var phone: String,
    var birth: LocalDateTime,
    var city: String,
    var country: String,
    var linkedin: String?,
    var github: String?,
    var cv: String,
    @OneToMany var technologyRelations: List<CandidateTechnology> = emptyList(),
    @Id @GeneratedValue var id: Long? = null,
    @CreationTimestamp var createdAt: LocalDateTime? = null,
    @UpdateTimestamp var updatedAt: LocalDateTime? = null) {
    val technologies: List<Technology>
        get() = technologyRelations.map { it.technology }
}

@Entity
class Technology(
    var name: String,
    var description: String?,
    @OneToMany var candidateRelations: List<CandidateTechnology> = emptyList(),
    @Id @GeneratedValue var id: Long? = null,
    @CreationTimestamp var createdAt: LocalDateTime? = null,
    @UpdateTimestamp var updatedAt: LocalDateTime? = null) {
    val candidates: List<Candidate>
        get() = candidateRelations.map { it.candidate }
}

@Entity
class CandidateTechnology(
    @ManyToOne var candidate: Candidate,
    @ManyToOne var technology: Technology,
    level: Int,
    var note: String,
    @Id @GeneratedValue var id: Long? = null,
    @CreationTimestamp var createdAt: LocalDateTime? = null,
    @UpdateTimestamp var updatedAt: LocalDateTime? = null) {
    var level: Int = level.requiredInRange(1, 10)
        set(value) {
            field = value.requiredInRange(1, 10)
        }
}