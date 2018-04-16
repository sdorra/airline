<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:template match="/metadata/versioning">
      <xsl:for-each select="snapshotVersions">
        <xsl:for-each select="snapshotVersion">
          <xsl:if test="classifier/text() = 'CLASSIFIER'">
            <xsl:if test="extension = 'EXTENSION'">
              <xsl:value-of select="value"/>
            </xsl:if>
          </xsl:if>
        </xsl:for-each>
      </xsl:for-each>
  </xsl:template>
  <xsl:template match="groupId" />
  <xsl:template match="artifactId" />
  <xsl:template match="version" />
</xsl:stylesheet>
