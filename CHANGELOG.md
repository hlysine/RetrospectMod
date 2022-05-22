# Changelog

## [0.2.0] - Second Technical Test - 2022-05-22
### Mechanics
- Timelines now play cards one after the other instead of queueing all cards at once
- Reworded timeline descriptions and keywords
- Timelines can now hold and play timeline construction cards
- Added new card attribute Delusional
- Added new card attribute Paradoxical
- Add new card targeting: timeline targeting
- Allow base game characters to also have max 5 concurrent timelines
- Added Time Loop mechanic with proper VFX

### Cards
- Changed Misdirection to play the attack card before constructing a timeline
- Changed Tap The Future to shuffle 1 instead of 2 Empty cards
- Fixed Rewrite History not exhausting
- Reworked Shadow Form to increase card draw by a constant amount instead of scaling up per round
- Changed Empty to be upgradable
- Added new status card Paradox
- Changed Sleight Of Hand to be able to replay other Sleight Of Hand cards
- Added Paradoxical to Sleight Of Hand
- Reworked Into The Void to target one timeline and no longer exhaust
- Added new cards: 
  - Time Loop
  - Shortcut
  - Electric Shock
  - Avert
  - Tactical Retreat
  - Lightspeed Leap
  - Fast Forward
  - All-Out Attack
  - Warm Up
  - Telekinesis
  - Recollection
  - Ambush
  - Hat Trick
  - Blind Strike
  - Evanescence
  - Soulless Demon
  - Noxious Wraith
  - Defective Echo
  - Divine Eye

### Events
- Added Mysterious Prophet event

### Powers
- Powers that play cards at the end of a turn now play them sequentially, right before timelines do
- Frozen power now allows all auto-played cards to be played
- Added new powers:
  - Defective Echo (card power)
  - Energized (card power, same as base game power but use custom energy icon)
  - Evanescence (card power)
  - Shortcut (card power)
  - Time Loop (card power)
  - Warm Up (card power)

### VFX
- Changed spire heart attack sequence slightly
- Added a VFX effect in Misdirection to show the chosen card before playing it
- Added death animation to timelines
- Changed timelines to use the same models, animations and corpse images as the selected character
- Fixed incorrect z-order of hovered timeline cards
- Timeline cards now appear at the timeline before hovering above the timeline instead of appearing at the discard pile
- Hide timeline cards when the player is dead
- Added a VFX effect in Sleight Of Hand to show the chosen card before playing it
- Corrected turn-based indicator for some powers
- Corrected buff/debuff VFX for some powers

### Misc
- Updated author field in code
- Updated base game to version 03-07-2022
- Updated dependencies

## [0.0.3] - Balancing Changes - 2022-04-08
### New content
- Added spire heart finisher sequence
- Added new relic: Slanted Mirror

### Changes
- Added additional safety checks in Alternative Reality
- Renamed Breakneck Form to Shadow Form and reworked it
- Reworked Rewrite History
- Timelines now cost 40% of your current HP
- Timeline construction cards can now be played even if the timeline cannot be constructed
- Updated the list of authors
- Changed the design of energy panel
- Changed energy orb icons for cards
- Highlight the actual action in Alternative Reality
- Remove `_V` suffix from Strike and Defend
- Cards are no longer marked as seen by default
- Changed Empty to be a colorless card
- Fixed timeline placement when the player is surrounded
- Increased starting HP from 65 to 70
- Decreased maximum number of timelines from 10 to 5
- Set Divert to be the default card for Match and Keep event
- Set ascension HP loss to 4
- Removed 1 Strike and 1 Defend from starting deck, making the starting deck size now 10
- Renamed Perpetual Hourglass to Quantum Hourglass and changed its flavor

## [0.0.2] - Hotfix - 2022-04-01
### Changed
- Updated strings
- Prevent the use of timeline potion if a timeline cannot be created

## [0.0.1] - First Technical Test - 2022-03-31
### New content
- First technical test
  - Timeline mechanics
  - 21 cards
  - 1 potion
  - 3 relics
