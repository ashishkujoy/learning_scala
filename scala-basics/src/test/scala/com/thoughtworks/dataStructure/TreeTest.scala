package com.thoughtworks.dataStructure
import com.thoughtworks.BaseTest

class TreeTest extends BaseTest {
  private val leaf   = Leaf(3)
  private val branch = Branch(leaf, leaf)

  test("size of a leaf should be one") {
    leaf.size shouldBe 1
  }

  test("size of branch with two leaf should be two") {
    branch.size shouldBe 2
  }

  test("should give size of nested tree") {
    val nestedTree = Branch(branch, Branch(leaf, branch))
    nestedTree.size shouldBe 5
  }

  test("should give maximum element of a leaf") {
    val leaf = Leaf(2)
    Tree.max(leaf) shouldBe 2
  }

  test("should give maximum element of a branch of two leafs") {
    val branch = Branch(Leaf(2), Leaf(30))
    Tree.max(branch) shouldBe 30
  }

  test("should give maximum element of a nested tree") {
    val nestedBranch = Branch(Leaf(2), Branch(Leaf(0), Leaf(21)))
    Tree.max(nestedBranch) shouldBe 21
  }

  test("should give depth of leaf") {
    leaf.depth shouldBe 0
  }

  test("should give depth of branch of two leafs") {
    branch.depth shouldBe 1
  }

  test("should give depth of nested tree") {
    val nestedBranch = Branch(leaf, Branch(leaf, branch))
    nestedBranch.depth shouldBe 3
  }

  test("should map leaf as per given mapper") {
    leaf.map(_.toString) shouldBe Leaf("3")
  }

  test("should map branch as per given mapper") {
    branch.map(_.toString) shouldBe Branch(Leaf("3"), Leaf("3"))
  }

  test("should map nested tree") {
    val stringLeaf       = Leaf("3")
    val stringBranch     = Branch(stringLeaf, stringLeaf)
    val nestedStringTree = Branch(stringLeaf, Branch(stringLeaf, stringBranch))
    val nestedBranchTree = Branch(leaf, Branch(leaf, branch))

    nestedBranchTree.map(_.toString) shouldBe nestedStringTree
  }

}
