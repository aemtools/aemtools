package com.aemtools.blocks.rename.model.fixture

/**
 * @author Dmytro Troynikov
 */
interface IRenameFixture {

  /**
   * Set the state which should be before the rename
   *
   * @param state the "before" state
   */
  fun before(state: IRenameState.() -> Unit)

  /**
   * Rename element under caret to given name.
   *
   * @param name new name
   */
  fun renameTo(name: String)

  /**
   * Set the state which should be after rename process.
   *
   * @param state object which describes the state after rename
   */
  fun after(state: IRenameState.() -> Unit)

}
