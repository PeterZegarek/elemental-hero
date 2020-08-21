---
layout: default
title: Player
nav_order: 4
parent: Game Details
has_children: true
permalink: /GameDetails/Player
---

# Navigation Structure
{: .no_toc }

## Table of contents
{: .no_toc .text-delta }

1. TOC
{:toc}

---

# Player

## What does the Player class do?

The `Player` class (found in the `Scene` package) is responsible for everything to do with the player (for this game, the player is the cat) 
during the platformer game except for the player character, which includes the player's graphics, movement, and interactions with the map (such as collision detection).

In the gif below, everything the cat is doing and how it is interacting with the map is handled by the `Player` class.

![game-screen-1.gif](../../assets/images/playing-level.gif)