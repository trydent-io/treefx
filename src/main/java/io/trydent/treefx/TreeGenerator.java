/*
 * Copyright (c) 2012, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */
package io.trydent.treefx;

import javafx.scene.Group;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class TreeGenerator {

  public static final int FLOWERS_NUMBER = 100;
  public int flowersNumber = FLOWERS_NUMBER;
  public Group content;
  public int treeDepth;

  public TreeGenerator(Group content, int treeDepth) {
    this.content = content;
    this.treeDepth = treeDepth;
  }

  public Tree generateTree() {
    final Tree tree = new Tree(treeDepth);
    Util.addChildToParent(content, tree);

    final Branch root = new Branch();
    Util.addChildToParent(tree, root);
    tree.generations.get(0).add(root); //root branch

    for (int i = 1; i < treeDepth; i++) {
      for (Branch parentBranch : tree.generations.get(i - 1)) {
        final List<Branch> newBranches = generateBranches(parentBranch, i);
        if (newBranches.isEmpty()) {
          tree.crown.add(parentBranch);
        }
        tree.generations.get(i).addAll(generateBranches(parentBranch, i));
      }
    }
    tree.crown.addAll(tree.generations.get(treeDepth - 1));
    tree.leafage.addAll(generateLeafage(tree.crown));
    tree.flowers.addAll(generateFlowers(tree.crown));
    return tree;
  }

  private List<Branch> generateBranches(Branch parentBranch, int depth) {
    List<Branch> branches = new ArrayList<>();
    if (parentBranch == null) { // add root branch
      branches.add(new Branch());
    } else {
      if (parentBranch.length < 10) {
        return Collections.emptyList();
      }
      branches.add(new Branch(parentBranch, Branch.Type.LEFT, depth)); //add side left branch
      branches.add(new Branch(parentBranch, Branch.Type.RIGHT, depth)); // add side right branch
      branches.add(new Branch(parentBranch, Branch.Type.TOP, depth)); //add top branch
    }

    return branches;
  }

  private List<Leaf> generateLeafage(List<Branch> crown) {
    List<Leaf> leafage = new ArrayList<>();
    for (final Branch branch : crown) {
      Leaf leaf = new Leaf(branch);
      leafage.add(leaf);
      Util.addChildToParent(branch, leaf);
    }
    return leafage;
  }

  private List<Flower> generateFlowers(List<Branch> crown) {
    List<Flower> flowers = new ArrayList<>(flowersNumber);
    for (int i = 0; i < flowersNumber; i++) {
      Branch branch = crown.get(RandomUtil.getRandomIndex(0, crown.size() - 1));
      final Flower flower = new Flower(branch);
      Util.addChildToParent(branch, flower);
      flowers.add(flower);
    }
    return flowers;
  }
}
