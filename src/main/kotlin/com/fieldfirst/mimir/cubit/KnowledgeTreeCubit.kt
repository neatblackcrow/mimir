package com.fieldfirst.mimir.cubit

import com.fieldfirst.mimir.database.*
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import javax.swing.tree.DefaultMutableTreeNode

sealed class EditState

class Initial(val rootNode: DefaultMutableTreeNode) : EditState()

class KnowledgeTreeCubit : BaseCubit<EditState>() {
    override fun initialState(): EditState = Initial(buildKnowledgeTree(1))

    private fun buildKnowledgeTree(categoryId: Int = 1): DefaultMutableTreeNode {
        val parentNode: DefaultMutableTreeNode
        val parentCategory = transaction {
            Categories.select { Categories.id eq categoryId }.single().let {
                Categories.fromResultRow(it)
            }
        }
        parentNode = DefaultMutableTreeNode(parentCategory)

        val childCategories: List<Category> = transaction {
            Categories.select { Categories.parentCategory eq parentCategory.id }.map {
                Categories.fromResultRow(it)
            }
        }
        val childCards: List<Card> = transaction {
            Cards.select { Cards.category eq parentCategory.id }.map {
                Cards.fromResultRow(it)
            }
        }
        val items: List<Order> = childCategories + childCards
        for (item in items.sortedBy { it.ordered }) {
            when (item) {
                is Card -> parentNode.add(DefaultMutableTreeNode(item))
                is Category -> parentNode.add(buildKnowledgeTree(item.id))
            }
        }
        return parentNode
    }
}