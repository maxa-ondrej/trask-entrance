package cz.majksa.trask.entrance

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

/**
 * Represents a single candidate with all the information about them.
 * Can be bound to any technology using [technologies] field.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author Ondřej Maxa
 */
@Entity
class Candidate(
    name: String,
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
) {
    var name: String = name.requiredNotBlank()
        set(value) {
            field = value.requiredNotBlank()
        }
}

/**
 * Represents a single technology that can be bound to a [Candidate].
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author Ondřej Maxa
 */
@Entity
class Technology(
    name: String,
    @OneToMany(
        cascade = [CascadeType.ALL],
        mappedBy = "technology"
    ) var candidates: Set<CandidateTechnology> = mutableSetOf(),
    @Id @GeneratedValue var id: Long? = null,
    @CreationTimestamp var createdAt: LocalDateTime? = null,
    @UpdateTimestamp var updatedAt: LocalDateTime? = null
) {
    var name: String = name.requiredNotBlank()
        set(value) {
            field = value.requiredNotBlank()
        }
}

/**
 * Represents a binding between a [Candidate] and a [Technology].
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author Ondřej Maxa
 */
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
