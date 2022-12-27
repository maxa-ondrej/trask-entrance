package cz.majksa.trask.entrance

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
class Candidate(
    var name: String,
    var surname: String,
    var email: String,
    var phone: String,
    var city: String,
    var country: String,
    var linkedin: String?,
    var github: String?,
    @OneToMany(
        cascade = [CascadeType.ALL],
        mappedBy = "candidate"
    ) var technologies: Set<CandidateTechnology> = mutableSetOf(),
    @Id @GeneratedValue var id: Long? = null,
    @CreationTimestamp var createdAt: LocalDateTime? = null,
    @UpdateTimestamp var updatedAt: LocalDateTime? = null
)

@Entity
class Technology(
    var name: String,
    @OneToMany(
        cascade = [CascadeType.ALL],
        mappedBy = "technology"
    ) var candidates: Set<CandidateTechnology> = mutableSetOf(),
    @Id @GeneratedValue var id: Long? = null,
    @CreationTimestamp var createdAt: LocalDateTime? = null,
    @UpdateTimestamp var updatedAt: LocalDateTime? = null
)

@Entity
class CandidateTechnology(
    @ManyToOne(
        cascade = [CascadeType.ALL],
        optional = false
    ) @JoinColumn(name = "candidate_id") var candidate: Candidate,
    @ManyToOne(
        cascade = [CascadeType.ALL],
        optional = false
    ) @JoinColumn(name = "technology_id") var technology: Technology,
    level: Int,
    var note: String?,
    @Id @GeneratedValue var id: Long? = null,
    @CreationTimestamp var createdAt: LocalDateTime? = null,
    @UpdateTimestamp var updatedAt: LocalDateTime? = null
) {
    var level: Int = level.requiredInRange(1, 10)
        set(value) {
            field = value.requiredInRange(1, 10)
        }
}
