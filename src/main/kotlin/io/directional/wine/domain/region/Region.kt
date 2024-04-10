package io.directional.wine.domain.region

import io.directional.wine.domain.BaseEntity
import io.directional.wine.domain.vo.Name
import jakarta.persistence.*

@Entity
@Table(name = "region")
class Region(
    name:Name,
    rootId:Long?,
    parent: Region?,
):BaseEntity() {

    @Embedded
    var name: Name = name
        protected set

    @Column(nullable = false)
    var depth: Int = 1
        protected set
    @Column(name = "root_id")
    var rootId: Long? = rootId
        protected set
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    var parent: Region? = parent // 부모 지역 연관
        protected set

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "parent")
    protected val mutableChildren: MutableList<Region> = mutableListOf()
    val children: List<Region> get() = mutableChildren.toList()

    companion object{
        fun createRegion(
            korName: String,
            engName: String,
            rootId: Long?,
            parent: Region? = null
        ) =
            Region(
                name = Name(korName,engName),
                rootId = rootId,
                parent = parent,
            )
    }
    fun updateDepth() {
        parent?.let {
            depth = it.depth + 1
        }
    }
    fun updateName(korName: String, engName: String) {
        name = Name(korName, engName)
    }
}
